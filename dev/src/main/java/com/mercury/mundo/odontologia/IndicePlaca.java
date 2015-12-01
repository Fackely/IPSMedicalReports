package com.mercury.mundo.odontologia;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.mercury.dao.odontologia.IndicePlacaDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;

public class IndicePlaca implements Serializable
{
	private int			 codigo;
	private String		  codTratamiento;
	private String		  observaciones;
	private String		  obsNuevas;
	private transient UsuarioBasico   medico;
	private String		  fecha;
	private Map			 dientesMap;
	private boolean		 enBD;
	private int 			numeroDientes;
	private int				numeroSuperficies;
	
	private boolean esImpresion;
	
	private static IndicePlacaDao indicePlacaDao;
	
	private static IndicePlacaDao getIndicePlacaDao()
	{
		if(indicePlacaDao==null)
		{
			String tipoBD = System.getProperty("TIPOBD" );
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			indicePlacaDao=myFactory.getIndicePlacaDao();
		}
		return indicePlacaDao;
	}
	
	public IndicePlaca()
	{
		this.codigo=-1;
		this.codTratamiento="";
		this.observaciones="";
		this.obsNuevas="";
		this.medico=new UsuarioBasico();
		this.fecha="";
		this.dientesMap= new HashMap();
		this.enBD=false;
		this.numeroDientes=0;
		this.numeroSuperficies=0;
		this.esImpresion=Boolean.FALSE;
	}
	
	public void setCodigo(int codigo)
	{
		this.codigo=codigo;
	}
	
	public int getCodigo()
	{
		return this.codigo;
	}
	
	public void setEnBD(boolean enBD)
	{
		this.enBD=enBD;
	}
	
	public boolean getEnBD()
	{
		return this.enBD;
	}
	
	public void setCodTratamiento(String codTratamiento)
	{
		this.codTratamiento=codTratamiento;
	}
	
	public String getCodTratamiento()
	{
		return this.codTratamiento;
	}
	
	public void setObservaciones(String observaciones)
	{
		this.observaciones = observaciones;
	}
	
	public String getObservaciones()
	{
		return this.observaciones;
	}
	
	public String getObsAnteriores()
	{
		return this.observaciones;
	}
	
	public void setObsAnteriores(String obsAnteriores)
	{
		this.observaciones=obsAnteriores;
	}
	
	public String getObsNuevas()
	{
		return this.obsNuevas;
	}
	
	public void setObsNuevas(String obsNuevas)
	{
		this.obsNuevas=obsNuevas;
	}
	
	public void setMedico(UsuarioBasico medico)
	{
		this.medico=medico;
	}
	
	public UsuarioBasico getMedico()
	{
		return this.medico;
	}
	
	public void setFecha(String fecha)
	{
		this.fecha=fecha;
	}
	
	public String getFecha()
	{
		return this.fecha;
	}
	
	public Object getDienteMap(String key)
	{
		return this.dientesMap.get(key);
	}
	
	public void setDienteMap(String key, Object value)
	{
		this.dientesMap.put(key, value);
	}
	
	public void insertar(Connection con) throws SQLException
	{
		this.codigo= IndicePlaca.getIndicePlacaDao().insertar(
				con,
				this.getCodTratamiento(),
				this.getObservaciones(),
				this.getMedico().getCodigoPersona(),
				this.getFecha(),
				this.getNumeroSuperficies(), 
				this.getNumeroDientes());
		
		for(int k=10; k<=80; k=k+10)
		{
			for(int i=1+k; i<=(k<=40?8:5)+k; i++)
			{
				for(int j=1; j<=4; j++) // Se recorren cada uno de los sectores quienes realmente tienen la información
				{
					String estadoSectorDiente = (String)this.getDienteMap("diente_"+i+"_sector_"+j);
					
					if(UtilidadTexto.getBoolean(estadoSectorDiente))
					{
						IndicePlaca.getIndicePlacaDao().insertarSectorDientePlaca(con, this.codigo, i, j);
					}
				}
			}
		}
	}
	
	public void modificar(Connection con) throws SQLException
	{
		this.codigo= IndicePlaca.getIndicePlacaDao().modificar(
				con,
				this.getCodigo(),
				this.getObservaciones());
	}

	public static Collection<HashMap<String, Object>> consultarIndicesPlacaTratamiento(Connection con, int codTratamiento) throws SQLException
	{
		return IndicePlaca.getIndicePlacaDao().consultarIndicesPlacaTratamiento(con, codTratamiento);
	}	
	
	public void consultar(Connection con, int codigo) throws SQLException
	{
		Collection<HashMap<String, Object>> resultado =IndicePlaca.getIndicePlacaDao().consultar(
				con, 
				codigo);
		Iterator<HashMap<String, Object>> iteradorResultado=resultado.iterator();
		
		if(iteradorResultado.hasNext())
		{
			HashMap<String, Object> mapaResultado=iteradorResultado.next();
			Utilidades.imprimirMapa(mapaResultado);
			this.setCodigo(codigo);
			this.setCodTratamiento(mapaResultado.get("cod_tratamiento_odo")+"");
			this.medico.cargarUsuarioBasico(con, Integer.parseInt(mapaResultado.get("cod_medico")+""));
			this.setObservaciones(mapaResultado.get("observaciones")+"");
			this.setFecha(mapaResultado.get("fecha")+"");
			this.setNumeroDientes(Integer.parseInt(mapaResultado.get("numero_dientes")+""));
			this.setEnBD(true);
			
			boolean mostrarIndice=UtilidadTexto.getBoolean(ValoresPorDefecto.getMostrarGraficaCalculoIndicePlaca(medico.getCodigoInstitucionInt()));
 
			if(mostrarIndice) {
				Collection<HashMap<String, Object>> sectores=IndicePlaca.getIndicePlacaDao().consultarSectoresDientesPlaca(con, codigo);
				Iterator<HashMap<String, Object>> iteradorSectores=sectores.iterator();
				while(iteradorSectores.hasNext())
				{
					HashMap<String, Object> mapaSectorDientePlaca=iteradorSectores.next();
				   	Utilidades.imprimirMapa(mapaSectorDientePlaca);
					String nombre = 
						"diente_"+
						mapaSectorDientePlaca.get("numero_diente")+""+
						"_sector_"+
						mapaSectorDientePlaca.get("numero_sector")+"";
					this.setDienteMap(nombre, "true");
				}
			}
				this.numeroSuperficies=IndicePlaca.getIndicePlacaDao().consultarNumeroSuperficies(con, codigo);
		}
		else
			throw new SQLException("No se encontró el indice de placa de codigo:"+codigo);
	}

	public int getNumeroDientes()
	{
		return numeroDientes;
	}

	public void setNumeroDientes(int numeroDientes)
	{
		this.numeroDientes = numeroDientes;
	}

	/**
	 * Obtiene el valor del atributo numeroSuperficies
	 *
	 * @return  Retorna atributo numeroSuperficies
	 */
	public int getNumeroSuperficies()
	{
		return numeroSuperficies;
	}

	/**
	 * Establece el valor del atributo numeroSuperficies
	 *
	 * @param valor para el atributo numeroSuperficies
	 */
	public void setNumeroSuperficies(int numeroSuperficies)
	{
		this.numeroSuperficies = numeroSuperficies;
	}

	/**
	 * Establece el valor del atributo esImpresion
	 *
	 * @param valor para el atributo esImpresion
	 */
	public void setEsImpresion(boolean esImpresion)
	{
		this.esImpresion = esImpresion;
	}

	/**
	 * Obtiene el valor del atributo esImpresion
	 *
	 * @return Retorna atributo esImpresion
	 */
	public boolean isEsImpresion()
	{
		return esImpresion;
	}

}
