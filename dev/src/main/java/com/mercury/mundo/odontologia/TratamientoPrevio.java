package com.mercury.mundo.odontologia;

public class TratamientoPrevio
{
    private int codigo;
    private String tipoTratamiento;
    private String fechaInicio;
    private String fechaFinalizacion;
    private String descripcion;
    
    public TratamientoPrevio()
    {
        this.codigo=-1;
        this.tipoTratamiento="";
        this.fechaInicio="";
        this.fechaFinalizacion="";
        this.descripcion="";
    }

    public TratamientoPrevio(int codigo, String tipoTratamiento, String fechaInicio, String fechaFinalizacion, String descripcion)
    {
        this.codigo=codigo;
        this.tipoTratamiento=tipoTratamiento;
        this.fechaInicio=fechaInicio;
        this.fechaFinalizacion=fechaFinalizacion;
        this.descripcion=descripcion;
    }
    
    public void setCodigo(int codigo)
    {
        this.codigo=codigo;
    }
    
    public int getCodigo()
    {
        return this.codigo;
    }
    
    public void setTipoTratamiento(String tipoTratamiento)
    {
        this.tipoTratamiento=tipoTratamiento;
    }
    
    public String getTipoTratamiento()
    {
        return this.tipoTratamiento;
    }
    
    public void setFechaInicio(String fechaInicio)
    {
        this.fechaInicio=fechaInicio;
    }
    
    public String getFechaInicio()
    {
        return this.fechaInicio;
    }

    public void setFechaFinalizacion(String fechaFinalizacion)
    {
        this.fechaFinalizacion=fechaFinalizacion;
    }
    
    public String getFechaFinalizacion()
    {
        return this.fechaFinalizacion;
    }

    public void setDescripcion(String descripcion)
    {
        this.descripcion=descripcion;
    }
    
    public String getDescripcion()
    {
        return this.descripcion;
    }
}
