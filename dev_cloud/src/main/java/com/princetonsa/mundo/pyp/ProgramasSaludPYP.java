/*
 * author armando
 */
package com.princetonsa.mundo.pyp;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.pyp.ProgramasSaludPYPDao;

/**
 * 
 * @author armando
 *
 */
public class ProgramasSaludPYP 
{

	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(ProgramasSaludPYP.class);
	
	/**
	 * 
	 */
	private ProgramasSaludPYPDao objetoDao;
	
	
	/**
	 * 
	 */
	private HashMap diagnosticos;
	
	/**
	 * 
	 */
	private HashMap programas;
	
	/**
	 * 
	 */
	private String codigo;
	
	/**
	 * 
	 */
	private String descripcion;
	
	/**
	 * 
	 */
	private String tipoPrograma;
	
	/**
	 * 
	 */
	private String grupoEtareo;
	
	/**
	 * 
	 */
	private boolean embarazo;
	
	/**
	 * 
	 */
	private String formato;
	
	/**
	 * 
	 */
	private String archivo;
	
	/**
	 * 
	 */
	private boolean activo;
	
	/**
	 * 
	 */
	private int institucion;
	
	
	/**
	 * constructor 
	 *
	 */
	public ProgramasSaludPYP()
	{
		this.reset();
		init(System.getProperty("TIPOBD"));
	}

	
	/**
	 * 
	 * @param property
	 */
	private boolean init(String tipoBD) 
	{
    	if ( objetoDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			objetoDao= myFactory.getProgramasSaludPYPDao();
			if( objetoDao!= null )
				return true;
		}
		return false;
	}


	/**
	 * 
	 *
	 */
	public void reset() 
	{
		this.programas =new HashMap();
		this.programas.put("numRegistros","0");
		this.codigo="";
		this.descripcion="";
		this.tipoPrograma="";
		this.grupoEtareo="";
		this.embarazo=false;
		this.formato="";
		this.archivo="";
		this.activo=false;
		this.institucion=ConstantesBD.codigoNuncaValido;
    	this.diagnosticos=new HashMap();
    	this.diagnosticos.put("numRegistros","0");
	}


	public boolean isActivo() {
		return activo;
	}


	public void setActivo(boolean activo) {
		this.activo = activo;
	}


	public String getArchivo() {
		return archivo;
	}


	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}


	public String getCodigo() {
		return codigo;
	}


	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public boolean isEmbarazo() {
		return embarazo;
	}


	public void setEmbarazo(boolean embarazo) {
		this.embarazo = embarazo;
	}


	public String getFormato() {
		return formato;
	}


	public void setFormato(String formato) {
		this.formato = formato;
	}


	public String getGrupoEtareo() {
		return grupoEtareo;
	}


	public void setGrupoEtareo(String grupoEtareo) {
		this.grupoEtareo = grupoEtareo;
	}


	public HashMap getProgramas() {
		return programas;
	}


	public void setProgramas(HashMap programas) {
		this.programas = programas;
	}


	public String getTipoPrograma() {
		return tipoPrograma;
	}


	public void setTipoPrograma(String tipoPrograma) {
		this.tipoPrograma = tipoPrograma;
	}


	public int getInstitucion() {
		return institucion;
	}


	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 */
	public void cargarInformacion(Connection con, int institucion) 
	{
		this.programas=(HashMap)objetoDao.cargarInfomacionBD(con,institucion);
	}


	/**
	 * 
	 * @param con
	 * @param string
	 * @param i
	 * @return
	 */
	public boolean eliminarRegistro(Connection con, String codigo, int institucion) {
		return objetoDao.eliminarRegistro(con,codigo,institucion);
	}

	/**
	 * 
	 * @param con
	 * @param codigo2
	 * @param institucion2
	 */
	public void cargarTipoPrograma(Connection con, String codigo, int institucion) 
	{
		ResultSetDecorator rs=objetoDao.cargarPrograma(con,codigo,institucion);
		try {
			while (rs.next())
			{
				this.codigo=rs.getString("codigo");
				this.institucion=rs.getInt("institucion");
				this.descripcion=rs.getString("descripcion");
				this.tipoPrograma=rs.getString("tipoprograma");
				this.grupoEtareo=rs.getString("grupoetareo");
				this.embarazo=rs.getBoolean("embarazo");
				this.formato=rs.getString("formato");
				this.archivo=rs.getString("archivo");
				this.activo=rs.getBoolean("activo");
			}
			this.diagnosticos=(HashMap)objetoDao.cargarDiagnosticosPrograma(con,this.codigo,this.institucion);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public boolean modificarRegistro(Connection con)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		transaccion=objetoDao.modificarRegistro(con,this.codigo,this.institucion,this.descripcion,this.tipoPrograma,this.grupoEtareo,this.embarazo,this.formato,this.archivo,this.activo);
		if(transaccion)
			this.guardarDiagnosticos(con);
		if(transaccion)
			UtilidadBD.finalizarTransaccion(con);
		else
			UtilidadBD.abortarTransaccion(con);
		return transaccion;
	}


	/**
	 * 
	 * @param con
	 * @return
	 */
	public boolean insertarRegistro(Connection con)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		transaccion=objetoDao.insertarRegistro(con,this.codigo,this.institucion,this.descripcion,this.tipoPrograma,this.grupoEtareo,this.embarazo,this.formato,this.archivo,this.activo);
		if(transaccion)
			this.guardarDiagnosticos(con);
		
		if(transaccion)
			UtilidadBD.finalizarTransaccion(con);
		else
			UtilidadBD.abortarTransaccion(con);
		return transaccion;
	}


	/**
	 * @param con 
	 * 
	 *
	 */
	private boolean guardarDiagnosticos(Connection con) 
	{
		boolean transaccion=true;
		for(int i=0;i<Integer.parseInt(this.diagnosticos.get("numRegistros")+"");i++)
		{
			if(!UtilidadTexto.getBoolean(this.diagnosticos.get("eliminado_"+i)+""))
			{
				if(!(this.diagnosticos.get("tiporegistro_"+i)+"").equalsIgnoreCase("BD"))
				{
					if(!objetoDao.guardarDiagnostico(con,this.codigo,this.institucion,(this.diagnosticos.get("acronimo_"+i)+""),(this.diagnosticos.get("cie_"+i)+"")))
					{
						transaccion=false;
						i=Integer.parseInt(this.diagnosticos.get("numRegistros")+"");
					}
				}
			}
			else if((this.diagnosticos.get("tiporegistro_"+i)+"").equalsIgnoreCase("BD"))
			{
				if(!objetoDao.eliminarDiagnostico(con,this.codigo,this.institucion,(this.diagnosticos.get("acronimo_"+i)+""),(this.diagnosticos.get("cie_"+i)+"")))
				{
					transaccion=false;
					i=Integer.parseInt(this.diagnosticos.get("numRegistros")+"");
				}
			}
		}
		return transaccion;
	}


	public HashMap getDiagnosticos() {
		return diagnosticos;
	}


	public void setDiagnosticos(HashMap diagnosticos) {
		this.diagnosticos = diagnosticos;
	}
	
	/**
	 * Método para saber si puedo eliminar un programa
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public boolean puedoEliminarPrograma(Connection con,String codigo,int institucion)
	{
		return objetoDao.puedoEliminarPrograma(con, codigo, institucion);
	}

}
