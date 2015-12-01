package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

/**
 * 
 * @author Alejandro Diaz Betancourt
 * @since Enero 24 de 2005
 * Esta clase contiene utilidades que permiten manipular imagenes
 */
public class Imagen
{ 
    /**
     * Metodo que permite exportar una cadena de pixeles a una imagen en formato PNG
     * @param cadenaPixeles cadena con la información de los pixeles
     * @param ancho ancho de la imagen
     * @param alto alto de la imagen
     * @param nombreArchivo nombre con el cual quedará el archivo de la imagen
     * @return true si fue exitosa la exportación, false en caso de error
     */
    public static boolean exportar(String cadenaPixeles, int ancho, int alto, File archivo)
    {
        int[] pixeles;
        
        
        String resultado[]=cadenaPixeles.split(",");
        pixeles = new int[ancho * alto];
        
        
        for(int i=0; i<resultado.length; i++)
        {
            if(!resultado[i].equals(""))
            {
                int tempo = Integer.parseInt(resultado[i]);
                
       
                pixeles[i]=tempo;
            }
        }
    
        try
        {
            BufferedImage imag = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
            imag.setRGB(0,0, ancho, alto, pixeles, 0, ancho);

            FileOutputStream out = new FileOutputStream(archivo);
            //ByteArrayOutputStream out = new ByteArrayOutputStream();
/*            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(imag);
            param.setQuality(1.0f, false);
            encoder.setJPEGEncodeParam(param);
            encoder.encode(imag);*/
            
            ImageIO.write(imag, "jpeg", out);

            /*String[] formatos = ImageIO.getReaderFormatNames();
            for(int i=0; i<formatos.length; i++)
            {
                Iterator iterador = ImageIO.getImageReadersByFormatName(formatos[i]);
                JPEGImageWriter coso = new JPEGImageWriter();
                coso.
                while(iterador.hasNext())
                {
                    Object lector = iterador.next();
                }
            }*/
            //ImageIO.write(imag, "jpg", new java.io.File(nombreArchivo));
        }
        catch(Exception e)
        {
            return false;
        }
        return true;
    }
    
    public static boolean exportar(String cadenaPixeles, int ancho, int alto, String rutaArchivo)
    {
        File archivo = new File(rutaArchivo);
        return Imagen.exportar(cadenaPixeles, ancho, alto, archivo);
    }
    
    public static String obtenerNombreUnicoArchivo(String encabezado, String extension)
    {
        String fechaActual = UtilidadFecha.getFechaActual().replace('/', '_');
        String horaActual = UtilidadFecha.getHoraActual().replace(':','_');
        String nombreArchivo = encabezado+"_"+fechaActual+"_"+horaActual+"."+extension;
        return nombreArchivo;
    }
}
