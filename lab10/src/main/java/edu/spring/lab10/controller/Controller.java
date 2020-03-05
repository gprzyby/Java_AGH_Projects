package edu.spring.lab10.controller;

import edu.spring.lab10.model.PicturesMemory;
import org.opencv.core.*;

import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "image")
public class Controller {

    private PicturesMemory memory;

    @Autowired
    public Controller(PicturesMemory memory) {
        this.memory = memory;
    }

    @PostMapping(value = "/")
    public ResponseEntity<Object> getImage(HttpServletRequest request) {
        try {
            BufferedImage image = ImageIO.read(request.getInputStream());
            String imageID = memory.addPicture(image);
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();
            Map<String, String> toRet = new HashMap<>();
            toRet.put("id",imageID);
            toRet.put("width", Integer.valueOf(imageWidth).toString());
            toRet.put("height", Integer.valueOf(imageHeight).toString());
            return new ResponseEntity<>(toRet,HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteImage(@PathVariable String id) {
        if(memory.deletePicture(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No image exists at that id!");
        }
    }

    @GetMapping(value = "{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable String id) {
        try{
            BufferedImage img = memory.getPicture(id);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(img,"jpg",outputStream);
            return new ResponseEntity<>(outputStream.toByteArray(),HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No image exists at that id!");
        }
    }

    @GetMapping(value = "/{id}/size")
    public ResponseEntity<Object> getPictureSize(@PathVariable String id) {
        if(!memory.checkIfExists(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No image exists at that id!");
        BufferedImage img = memory.getPicture(id);

        Map<String, String> toRet = new HashMap<>();
        toRet.put("width", Integer.valueOf(img.getWidth()).toString());
        toRet.put("height", Integer.valueOf(img.getHeight()).toString());
        return new ResponseEntity<>(toRet, HttpStatus.OK);

    }

    @GetMapping(value = "/{id}/scale/{percent1}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getPictureWithScale(@PathVariable String id, @PathVariable String percent1) throws IOException {
        int percent = Integer.valueOf(percent1);
        if(percent < 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Percent cannot be less than zero!");
        if(!memory.checkIfExists(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No image exists at that id!");
        BufferedImage img = memory.getClonedPicture(id);

        Mat mat = imgToMat(img);

        Mat mat1 = new Mat((int)(Math.ceil(img.getHeight() * (percent/100.0D))), (int)(Math.ceil(img.getWidth() * (percent/100.0D))), mat.type());
        Imgproc.resize(mat,mat1,mat1.size(),percent/100.0D,percent/100.0D,Imgproc.INTER_CUBIC);
        System.err.println(mat1.elemSize());
        byte[] toRet = new byte[mat1.cols() * mat1.rows() * (int)mat1.elemSize()];
        mat1.get(0,0,toRet);

        BufferedImage imgToRet = matToImg(mat1);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(imgToRet,"jpg",outputStream);
        return new ResponseEntity<>(outputStream.toByteArray(),HttpStatus.OK);
    }

    @GetMapping(value = "{id}/greyscale", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getPictureInGreyScale(@PathVariable String id) throws IOException {
        if(!memory.checkIfExists(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No image exists at that id!");
        BufferedImage img = memory.getClonedPicture(id);

        for(int i=0;i<img.getHeight();++i) {
            for(int j=0;j<img.getWidth();++j) {
                Color c = new Color(img.getRGB(j,i));
                int red = (int)(c.getRed() * 0.299);
                int green = (int)(c.getGreen() * 0.587);
                int blue = (int)(c.getBlue() * 0.114);
                int sumOfColors = red + green + blue;
                Color newColor = new Color(sumOfColors,sumOfColors,sumOfColors);
                img.setRGB(j,i,newColor.getRGB());
            }
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(img,"jpg",outputStream);
        return new ResponseEntity<>(outputStream.toByteArray(),HttpStatus.OK);
    }

    @GetMapping(value = "{id}/blur/{radius}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImageWithBlur(@PathVariable String id, @PathVariable int radius) throws IOException {
        if(!memory.checkIfExists(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No image exists at that id!");
        if(radius < 0 || (radius & 1) == 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Radius mustn't be power of 2");
        Mat source = imgToMat(memory.getClonedPicture(id));

        Mat destination = new Mat(source.rows(),source.cols(),source.type());
        Imgproc.GaussianBlur(source,destination,new Size(radius,radius), 0);
        BufferedImage img = matToImg(destination);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(img,"jpg",outputStream);
        return new ResponseEntity<>(outputStream.toByteArray(),HttpStatus.OK);
    }

    @GetMapping(value = "{id}/crop/{start}/{stop}/{width}/{height}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getCroppedImage(@PathVariable String id, @PathVariable int start, @PathVariable int stop,
                                                  @PathVariable int width, @PathVariable int height) throws IOException {
        if(!memory.checkIfExists(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No image exists at that id!");
        if(start < 0 || stop < 0 || width <= 0 || height <= 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Typed parameters cannot be less than zero!");
        BufferedImage img = memory.getClonedPicture(id);
        //x
        if(start + width > img.getWidth() || stop + height > img.getHeight()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Parameters gets out of picture bounds");
        Rect rect = new Rect(start,stop,width,height);
        Mat mat = new Mat(imgToMat(img),rect);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(matToImg(mat),"jpg",outputStream);
        return new ResponseEntity<>(outputStream.toByteArray(),HttpStatus.OK);

    }

    @GetMapping(value = "{id}/histogram")
    public ResponseEntity<Object> getImageHistogram(@PathVariable String id) {
        if(!memory.checkIfExists(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No image exists at that id!");
        int[] tabRed = new int[256];
        int[] tabGreen = new int[256];
        int[] tabBlue = new int[256];
        BufferedImage img = memory.getPicture(id);
        for(int y=0;y<img.getHeight();++y) {
            for(int x = 0; x < img.getWidth(); ++x) {
                Color color = new Color(img.getRGB(x,y));
                int blue = color.getBlue();
                int red = color.getRed();
                int green = color.getGreen();
                ++tabBlue[blue];
                ++tabGreen[green];
                ++tabRed[red];
            }
        }
        final int pixelAmount = img.getWidth() * img.getHeight();
        double[] redNorm = Arrays.stream(tabRed).asDoubleStream().map(e -> e/pixelAmount).toArray();
        double[] greenNorm = Arrays.stream(tabGreen).asDoubleStream().map(e -> e/pixelAmount).toArray();
        double[] blueNorm = Arrays.stream(tabGreen).asDoubleStream().map(e -> e/pixelAmount).toArray();

        //Preparing return
        Map<String,Map<Integer,Double>> toRet = new TreeMap<>();
        toRet.put("R", Controller.arrayToMap(redNorm));
        toRet.put("G", Controller.arrayToMap(greenNorm));
        toRet.put("B", Controller.arrayToMap(blueNorm));
        return new ResponseEntity<>(toRet,HttpStatus.OK);
    }

    public static Mat imgToMat(BufferedImage in) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(in, "jpg", byteArrayOutputStream);
            byteArrayOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
    }

    public static BufferedImage matToImg(Mat in) {
        MatOfByte mob=new MatOfByte();
        Imgcodecs.imencode(".jpg", in, mob);
        try {
            return ImageIO.read(new ByteArrayInputStream(mob.toArray()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Map<Integer, Double> arrayToMap(double[] array) {
        Map<Integer, Double> toRet = new TreeMap<>();
        for(int i=0;i<array.length;++i) {
            toRet.put(i,array[i]);
        }
        return toRet;
    }
}
