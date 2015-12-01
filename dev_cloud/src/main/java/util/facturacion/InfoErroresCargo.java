package util.facturacion;

import java.io.Serializable;
import java.util.Vector;

/**
 * 
 * @author wilson
 *
 */
public class InfoErroresCargo implements Serializable   
{
	private static final long serialVersionUID = 7520584846246399695L;

	/**
	 * 
	 */
	Vector mensajesErrorDetalle= new Vector<String>();
	
	/**
	 * 
	 */
	boolean tieneErrores;
	
	/**
	 * 
	 */
	boolean tieneErroresCodigo;

	/**
	 * 
	 * @param mensajesErrorDetalle
	 * @param sinErrores
	 */
	public InfoErroresCargo(Vector mensajesErrorDetalle, boolean tieneErrores) 
	{
		super();
		this.mensajesErrorDetalle = mensajesErrorDetalle;
		this.tieneErrores = tieneErrores;
		this.tieneErroresCodigo= false;
	}
	
	/**
	 * 
	 * @param mensajesErrorDetalle
	 * @param sinErrores
	 */
	public InfoErroresCargo() 
	{
		this.mensajesErrorDetalle = new Vector<String>();
		this.tieneErrores = false;
		this.tieneErroresCodigo=false;
	}

	/**
	 * @return the mensajesErrorDetalle
	 */
	public Vector getMensajesErrorDetalle() {
		return mensajesErrorDetalle;
	}

	/**
	 * @param mensajesErrorDetalle the mensajesErrorDetalle to set
	 */
	public void setMensajesErrorDetalle(Vector mensajesErrorDetalle) {
		this.mensajesErrorDetalle = mensajesErrorDetalle;
	}

	/**
	 * @return the mensajesErrorDetalle
	 */
	public String getMensajesErrorDetalle(int pos) {
		return mensajesErrorDetalle.get(pos).toString();
	}

	/**
	 * @param mensajesErrorDetalle the mensajesErrorDetalle to set
	 */
	public void setMensajesErrorDetalle(String value) {
		this.mensajesErrorDetalle.add(value);
	}

	/**
	 * @return the tieneErrores
	 */
	public boolean getTieneErrores() 
	{
		if(mensajesErrorDetalle.size()>0)
			return true;
		else
			return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public String logErrores()
	{
		String erroresLog= "\n";
		for(int w=0; w<this.getMensajesErrorDetalle().size();w++)
		{
			erroresLog+="error "+w+"--> "+this.getMensajesErrorDetalle(w);
		}
		erroresLog+="\n";
		return erroresLog;
	}

	/**
	 * @return the tieneErroresCodigo
	 */
	public boolean tieneErroresCodigo() {
		return tieneErroresCodigo;
	}

	/**
	 * @return the tieneErroresCodigo
	 */
	public boolean getTieneErroresCodigo() {
		return tieneErroresCodigo;
	}
	
	/**
	 * @param tieneErroresCodigo the tieneErroresCodigo to set
	 */
	public void setTieneErroresCodigo(boolean tieneErroresCodigo) {
		this.tieneErroresCodigo = tieneErroresCodigo;
	}

}