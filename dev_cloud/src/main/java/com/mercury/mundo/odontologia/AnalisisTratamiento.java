package com.mercury.mundo.odontologia;

import util.InfoDatos;
import util.InfoDatosInt;
import java.io.Serializable;

public class AnalisisTratamiento implements Serializable
{
    private InfoDatosInt    tipoAnalisis;
    private InfoDatos       opcionTipoAnalisis;
    private String          comentario;
    private String          observaciones;
    private boolean         enBD;
    
    public AnalisisTratamiento()
    {
        this.tipoAnalisis = new InfoDatosInt();
        this.opcionTipoAnalisis = new InfoDatos();
        this.comentario="";
        this.observaciones="";
        this.enBD=false;
    }
    
    public InfoDatosInt getTipoAnalisis()
    {
        return this.tipoAnalisis;
    }
    
    public void setTipoAnalisis(InfoDatosInt tipoAnalisis)
    {
        this.tipoAnalisis=tipoAnalisis;
    }
    
    public void setTipoAnalisis(int codigo, String nombre)
    {
        this.tipoAnalisis.setCodigo(codigo);
        this.tipoAnalisis.setNombre(nombre);
    }
    
    public InfoDatos getOpcionTipoAnalisis()
    {
        return this.opcionTipoAnalisis;
    }
    
    public void setOpcionTipoAnalisis(InfoDatos opcionTipoAnalisis)
    {
        this.opcionTipoAnalisis=opcionTipoAnalisis;
    }
    
    public void setOpcionTipoAnalisis(String id, String nombre)
    {
        this.opcionTipoAnalisis.setId(id);
        this.opcionTipoAnalisis.setNombre(nombre);
    }
    
    public String getComentario()
    {
        return this.comentario;
    }
    
    public void setComentario(String comentario)
    {
        this.comentario=comentario;
    }
    
    public String getObservaciones()
    {
        return this.observaciones;
    }
    
    public void setObservaciones(String observaciones)
    {
        this.observaciones=observaciones;
    }
    
    public boolean getEnBD()
    {
        return this.enBD;
    }
    
    public void setEnBD(boolean enBD)
    {
        this.enBD=enBD;
    }
}
