package com.mercury.mundo.odontologia;

public class SectorDiente
{
    private int numero;
    private int codEstadoInst;
    private int codEstado;
    private int numFotograma;
    private String nomEstado;
    
    public final static int CARIES = 3;
    public final static int AMALGAMAINADECUADA = 6;
    public final static int RESINAINADECUADA = 7;
    public final static int INCRUSTACIONINADECUADA = 9;
    
    public final static int AMALGAMA = 4;
    public final static int RESINA = 5;
    public final static int INCRUSTACION = 8;

    public SectorDiente()
    {
        this.numero=-1;
        this.codEstadoInst=1;
        this.codEstado=1;
        this.numFotograma=1;
        this.nomEstado="";
    }
    
    public int getNumero()
    {
        return this.numero;
    }
    
    public int getCodEstadoInst()
    {
        return this.codEstadoInst;
    }
    
    public String getNomEstado()
    {
        return this.nomEstado;
    }
    
    public int getNumFotograma()
    {
        return this.numFotograma;
    }
    
    public void setNumero(int numero)
    {
        this.numero=numero;
    }
    
    public void setCodEstadoInst(int codEstadoInst)
    {
        this.codEstadoInst=codEstadoInst;
    }
    
    public void setNomEstado(String nomEstado)
    {
        this.nomEstado=nomEstado;
    }
    
    public void setNumFotograma(int numFotograma)
    {
        this.numFotograma=numFotograma;
    }

	/**
	 * @return Returns the codEstado.
	 */
	public int getCodEstado() 
	{
		return codEstado;
	}

	/**
	 * @param codEstado The codEstado to set.
	 */
	public void setCodEstado(int codEstado) 
	{
		this.codEstado = codEstado;
	}
}
