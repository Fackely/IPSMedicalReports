package com.mercury.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.mercury.dto.odontologia.DtoOtroHallazgo;
import com.mercury.dto.odontologia.DtoPiezaDental;
import com.mercury.dto.odontologia.DtoSuperficie;
import com.mercury.mundo.odontologia.DienteOdontograma;
import com.mercury.mundo.odontologia.Odontograma;
import com.mercury.mundo.odontologia.SectorDiente;
import com.mercury.util.UtilidadBaseDatos;
import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.manejoPaciente.DtoOtrosIngresosPaciente;
import com.princetonsa.dto.odontologia.DtoHallazgoOdontologico;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.odontologia.DtoOdontograma;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoOdo;


public class SqlBaseOdontogramaDao
{
    /**
    * Objeto para manejar los logs de esta clase
    */
    private static Logger logger = Logger.getLogger(SqlBaseOdontogramaDao.class);
    
    private final static String insertarOdontogramaStr=
        "insert into historiaclinica.odontograma (" +
        "codigo, " +
        "cod_tratamiento_odo, " +
        "observaciones, " +
        "cod_medico, " +
        "fecha," +
        "xml, " +
        " imagen ) " +
        "values (?, ?, ?, ?, ?, ? , ? )";
    
    private final static String consultarOdontogramasTratamientoStr=
        "select " +
        "codigo as codigo, " +
        "observaciones as observaciones, " +
        "cod_medico as cod_medico, " +
        "to_char(fecha, 'DD/MM/YYYY') as fecha ," +
        "imagen as imagen " +
        "from historiaclinica.odontograma where " +
        "cod_tratamiento_odo = ? order by codigo";
    
    private final static String consultarOdontogramaStr=
        "select "+
        "codigo as codigo, " +
        "cod_tratamiento_odo as codTratamientoOdo, " +
        "observaciones as observaciones, " +
        "cod_medico as codMedico, " +
        "to_char(fecha, 'DD/MM/YYYY') as fecha, " +
        "xml AS xml ," +
        "imagen as imagen " +
        "from historiaclinica.odontograma where " +
        "codigo = ?";
    
    private final static String modificarOdontogramaStr=
        "update historiaclinica.odontograma " +
        "set observaciones=? where codigo=?";
    
    private final static String insertarDienteOdontogramaStr=
        "INSERT INTO val_diente (" +
            "cod_odontograma, " +
            "num_diente, " +
            "cod_estado_diente_inst) " +
            "values (?,?,?)";
                                                                                            
    private final static String consultarDientesOdontogramaStr=
        "SELECT " +
        "cod_odontograma as codOdontograma, " +
        "num_diente as numDiente, " +
        "cod_estado_diente_inst as codEstadoDienteInst, " +
        "getNumFotogramaEstadDienteInst(cod_estado_diente_inst) as numFotograma," +
        "getNombreEstadoDienteInst(cod_estado_diente_inst) as nomEstadoDiente, " +
        "getCodigoEstadoDienteInst(cod_estado_diente_inst) as codEstadoDiente " +
        "FROM val_diente where " +
        "cod_odontograma=?";
    
    private final static String insertarSectorDienteOdontogramaStr=
        "INSERT INTO val_sector_diente (" +
            "cod_odontograma, " +
            "num_diente, " +
            "num_sector, " +
            "cod_est_sec_diente_inst) " +
            "values (?,?,?,?)";
    
    private final static String consultarSectoresDienteOdontogramaStr=
        "SELECT " +
        "cod_odontograma as codOdontograma, " +
        "num_diente as numDiente, " +
        "num_sector as numSector, " +
        "cod_est_sec_diente_inst as codEstadoSectorDienteInst, " +
        "getNumFotogrEstadSecDienteInst(cod_est_sec_diente_inst) as numFotograma, " +
        "getNombEstadoSectorDienteInst(cod_est_sec_diente_inst) as nomEstadoSector, " +
        "cod_est_sec_diente_inst as codEstadoSector " +
        "from val_sector_diente " +
        "where " +
        "cod_odontograma=? and " +
        "num_diente=?";
    
    public static int obtenerCodigoSiguiente(Connection con, String secuencia) throws SQLException
    {
        String consultaSecuencia="SELECT "+secuencia;
        try
        {
            
            PreparedStatementDecorator obtenerCodigoStatement= new PreparedStatementDecorator(con.prepareStatement(consultaSecuencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ResultSetDecorator resultado=new ResultSetDecorator(obtenerCodigoStatement.executeQuery());
            if(resultado.next())
            {
                return resultado.getInt("codigo");
            }
            return 0;
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en la consulta del siguiente codigo de odontograma: SqlBaseOdontogramaDao "+e.toString());
            throw e;
        }
    }

    //Retorna el codigo del nuevo odontograma insertado
    public static int insertar(
            Connection con,
            String secuenciaCodigo,
            String codTratamientoOdo, 
            String observaciones,
            int codMedico, 
            String fecha,
            String xmlOdontograma,
            String imagen) throws SQLException
    {
        try
        {
        	if(System.getProperty("TIPOBD").equals("POSTGRESQL"))
    		{
	        	Statement stmt = con.createStatement();
	            stmt.execute("SET standard_conforming_strings TO off");
	            stmt.close();
    		}
        	
            int codigo = SqlBaseOdontogramaDao.obtenerCodigoSiguiente(con, secuenciaCodigo);
            PreparedStatementDecorator insertarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseOdontogramaDao.insertarOdontogramaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            insertarStatement.setInt(1, codigo);
            UtilidadBaseDatos.establecerParametro(2, Types.CHAR, codTratamientoOdo, insertarStatement);
            UtilidadBaseDatos.establecerParametro(3, Types.VARCHAR, observaciones, insertarStatement);
            insertarStatement.setInt(4, codMedico);
            UtilidadBaseDatos.establecerParametro(5, Types.DATE, fecha, insertarStatement);
            insertarStatement.setString(6, xmlOdontograma);
            
            
            if(!UtilidadTexto.isEmpty(imagen))
            {
            	insertarStatement.setString(7, imagen);
            }
            else
            {
            	insertarStatement.setNull(7, Types.VARCHAR);
            }
            
            
            insertarStatement.executeUpdate();
            return codigo;
        }
        catch(SQLException e)
        {
        	e.printStackTrace();
            logger.warn(e);
            throw e;
        }
    }
    
    public static ResultSetDecorator consultarOdontogramasTratamiento(Connection con, int codTratamiento) throws SQLException
    {
        try
        {
            PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseOdontogramaDao.consultarOdontogramasTratamientoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consultarStatement.setInt(1, codTratamiento);
            return new ResultSetDecorator(consultarStatement.executeQuery());
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }

    /**
     * 
     * @param con
     * @param codigo
     * @param objOdontograma
     * @throws SQLException
     */
    public static void consultar(Connection con, int codigo, Odontograma objOdontograma) throws SQLException
    {
        try
        {
            PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseOdontogramaDao.consultarOdontogramaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consultarStatement.setInt(1, codigo);
            ResultSetDecorator rs=new ResultSetDecorator(consultarStatement.executeQuery());
            
            
            if(rs.next())
            {
            	objOdontograma.setCodigo(codigo);
            	objOdontograma.setCodTratamiento(rs.getString("codTratamientoOdo"));
            	objOdontograma.getMedico().cargarUsuarioBasico(con, rs.getInt("codMedico"));
            	objOdontograma.setObservaciones(rs.getString("observaciones"));
            	objOdontograma.setFecha(rs.getString("fecha"));
            	objOdontograma.setXmlOdontograma(rs.getString("xml"));
            	objOdontograma.setImagen(rs.getString("imagen"));
            	objOdontograma.setHallazgosOtros(Odontograma.getOdontogramaDao().consultarHallazgosOtros(con, objOdontograma.getCodigo(), Odontograma.HALLAZGOS_OTROS));
            	objOdontograma.setHallazgosBoca(Odontograma.getOdontogramaDao().consultarHallazgosOtros(con, objOdontograma.getCodigo(), Odontograma.HALLAZGOS_BOCA));
            	objOdontograma.setEnBD(true);
                
                consultarDientes(con, codigo, objOdontograma);
                
            }
            else
                throw new SQLException("No se encontró el odontograma de codigo:"+codigo);
            rs.close();
            consultarStatement.close();
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }

    public static int modificar(
            Connection con, 
            int codigo, 
            String observaciones) throws SQLException
    {
        try
        {
            PreparedStatementDecorator modificarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseOdontogramaDao.modificarOdontogramaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));            
            UtilidadBaseDatos.establecerParametro(1, Types.VARCHAR, observaciones, modificarStatement);
            modificarStatement.setInt(2, codigo);
            return modificarStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }

    public static int insertarDiente(Connection con, int codOdontograma, int numDiente, int codEstadoDienteInst) throws SQLException
    {
        try
        {
            PreparedStatementDecorator insertarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseOdontogramaDao.insertarDienteOdontogramaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            insertarStatement.setInt(1, codOdontograma);
            insertarStatement.setInt(2, numDiente);
            insertarStatement.setInt(3, codEstadoDienteInst);
            return insertarStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    public static void consultarDientes(Connection con, int codOdontograma, Odontograma objOdontograma) throws SQLException
    {
        try
        {
                PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseOdontogramaDao.consultarDientesOdontogramaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                consultarStatement.setInt(1, codOdontograma);
                ResultSetDecorator rsDientes=new ResultSetDecorator(consultarStatement.executeQuery());
                while(rsDientes.next())
                {
                    DienteOdontograma diente = new DienteOdontograma();
                    diente.setNumDiente(rsDientes.getInt("numDiente"));                    
                    diente.setCodEstadoInst(rsDientes.getInt("codEstadoDienteInst"));
                    diente.setCodEstado(rsDientes.getInt("codEstadoDiente"));
                    diente.setNomEstado(rsDientes.getString("nomEstadoDiente"));
                    diente.setNumFotograma(rsDientes.getInt("numFotograma"));

                    consultarSectores(
                            con, 
                            codOdontograma,
                            diente.getNumDiente(), diente);
                    objOdontograma.setDienteMap("diente_"+diente.getNumDiente(), diente);
                }
                consultarStatement.close();
                rsDientes.close();

        }
        catch(SQLException e)
        {
            logger.error(e);
            throw e;
        }
    }
    
    public static int insertarSector(Connection con, int codOdontograma, int numDiente, int numSector, int codEstadoSectorInst) throws SQLException
    {
        try
        {
            PreparedStatementDecorator insertarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseOdontogramaDao.insertarSectorDienteOdontogramaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            insertarStatement.setInt(1, codOdontograma);
            insertarStatement.setInt(2, numDiente);
            insertarStatement.setInt(3, numSector);
            insertarStatement.setInt(4, codEstadoSectorInst);
            int retorna= insertarStatement.executeUpdate();
            insertarStatement.close();
            return retorna;
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }

    public static void consultarSectores(Connection con, int codOdontograma, int numDiente, DienteOdontograma diente) throws SQLException
    {
        try
        {
            PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseOdontogramaDao.consultarSectoresDienteOdontogramaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consultarStatement.setInt(1, codOdontograma);
            consultarStatement.setInt(2, numDiente);
            ResultSetDecorator rsSectores=new ResultSetDecorator(consultarStatement.executeQuery());
            while(rsSectores.next())
            {
                SectorDiente sector = new SectorDiente();
                sector.setNumero(rsSectores.getInt("numSector"));
                sector.setCodEstadoInst(rsSectores.getInt("codEstadoSectorDienteInst"));
                sector.setCodEstado(rsSectores.getInt("codEstadoSector"));
                sector.setNomEstado(rsSectores.getString("nomEstadoSector"));
                sector.setNumFotograma(rsSectores.getInt("numFotograma"));
                diente.setSector(sector);
            }
            rsSectores.close();
            consultarStatement.close();
        }
        catch(SQLException e)
        {
            logger.error(e);
            throw e;
        }
    }

    public static ResultSetDecorator consultarDientesPacientePorEstados(Connection con, int codPaciente, int[] estados) throws SQLException
    {
        String consultarDientesPacientePorEstadosStr=
            "select " +
            "num_diente as numeroDiente " +
            "from val_diente where (1<>1";
        
        for(int i=0; i<estados.length; i++)
        	consultarDientesPacientePorEstadosStr+=" or getCodigoEstadoDienteInst(cod_estado_diente_inst)="+estados[i];
        
        consultarDientesPacientePorEstadosStr+=") and " +
        		"cod_odontograma = " +
        		"(select max(odontograma.codigo) from odontograma, tratamiento_odo " +
        		"where " +
        		"odontograma.cod_tratamiento_odo = tratamiento_odo.codigo and " +
        		"cod_paciente=?)";
        try
        {
            PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(consultarDientesPacientePorEstadosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consultarStatement.setInt(1, codPaciente);
            return new ResultSetDecorator(consultarStatement.executeQuery());
        }
        catch(SQLException e)
        {
            logger.error(e);
            throw e;
        }
    }
	public static void insertarOtrosHallazgos(Connection con, ArrayList<DtoOtroHallazgo> hallazgosOtros, int codigoOdontograma)
	{
		try{
			String sentenciaIngreso="INSERT INTO " +
					"historiaclinica.otros_hallazgos (" +
						"codigo_pk, " +
						"pieza_dental, " +
						"superficie, " +
						"hallazgo, " +
						"fecha, " +
						"hora," +
						"odontograma)" +
					"VALUES(?,?,?,?,?,?,?)";
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentenciaIngreso);
			for(DtoOtroHallazgo hallazgo:hallazgosOtros)
			{
				int codigoPk=UtilidadBD.obtenerSiguienteValorSecuencia(con, "historiaclinica.seq_otros_hallazgos ");
				psd.setInt(1, codigoPk);
				if(hallazgo.getPieza()!=null && hallazgo.getPieza().getCodigoPk()>0)
				{
					psd.setInt(2, hallazgo.getPieza().getCodigoPk());
				}else
				{
					psd.setNull(2, Types.INTEGER);
				}
				if(hallazgo.getSuperficie()!=null && hallazgo.getSuperficie().getCodigoPk()>0)
				{
					psd.setInt(3, hallazgo.getSuperficie().getCodigoPk());
				}else
				{
					psd.setNull(3, Types.INTEGER);
				}
				psd.setInt(4, Integer.parseInt(hallazgo.getHallazgo().getConsecutivo()));
				psd.setString(5, UtilidadFecha.conversionFormatoFechaABD(hallazgo.getFecha()));
				psd.setString(6, hallazgo.getHora());
				psd.setInt(7, codigoOdontograma);
				psd.executeUpdate();
			}
			
		}catch (SQLException e) {
			logger.error("error insertando otros hallazgos", e);
		}
	}

	public static ArrayList<DtoOtroHallazgo> consultarHallazgosOtros(Connection con, int codigoOdontograma, byte constanteSeccionConsulta)
	{
		if(constanteSeccionConsulta==Odontograma.HALLAZGOS_OTROS)
		{
			String sentenciaConsulta=
				"SELECT " + 
					"oh.codigo_pk AS codigo_pk, " +  
					"oh.pieza_dental AS pieza_dental, " +
					"oh.superficie AS superficie, " +
					"oh.hallazgo AS hallazgo, " +
					"oh.fecha AS fecha, " +
					"oh.hora AS hora, " +
			        "sd.nombre AS nombre_superficie, " +
			        "pd.descripcion AS descripcion_pieza, " +
			        "hodo.nombre AS nombre_hallazgo, " +
			        "hodo.aplica_a AS aplica_a, " +
			        "codo.archivo_convencion AS archivoConvencion, " +
			        "codo.borde AS colorBorde " +
				"FROM " +
					"historiaclinica.otros_hallazgos oh " + 
				"INNER JOIN " +
					"odontologia.pieza_dental pd " +
						"ON(pd.codigo_pk=oh.pieza_dental) " +
				"LEFT OUTER JOIN " +
					"historiaclinica.superficie_dental sd " +
						"ON(sd.codigo=oh.superficie) " +
				"INNER JOIN " +
					"odontologia.hallazgos_odontologicos hodo " +
						"ON(hodo.consecutivo=oh.hallazgo) " +
				"LEFT JOIN " +
					"odontologia.convenciones_odontologicas codo " +
							"ON(codo.consecutivo=hodo.convencion) " +
				"WHERE " +
						"oh.odontograma=? " +
					"AND " +
						"oh.pieza_dental IS NOT NULL "+
					"AND " +
						"hodo.aplica_a in ('"+ConstantesIntegridadDominio.acronimoAplicaASuperficie+"','"+ConstantesIntegridadDominio.acronimoAplicaADiente+"')";

			return llenarArrayListDtos(sentenciaConsulta, con, codigoOdontograma);
		}
		if(constanteSeccionConsulta==Odontograma.HALLAZGOS_BOCA)
		{
			String sentenciaConsulta=
				"SELECT " + 
				"oh.codigo_pk AS codigo_pk, " +  
				"oh.pieza_dental AS pieza_dental, " +
				"oh.superficie AS superficie, " +
				"oh.hallazgo AS hallazgo, " +
				"oh.fecha AS fecha, " +
				"oh.hora AS hora, " +
		        "sd.nombre AS nombre_superficie, " +
		        "pd.descripcion AS descripcion_pieza, " +
		        "hodo.nombre AS nombre_hallazgo, " +
		        "hodo.aplica_a AS aplica_a, " +
		        "codo.archivo_convencion AS archivoConvencion, " +
		        "codo.borde AS colorBorde " +
			"FROM " +
				"historiaclinica.otros_hallazgos oh " + 
			"LEFT JOIN " +
				"odontologia.pieza_dental pd " +
					"ON(pd.codigo_pk=oh.pieza_dental) " +
			"LEFT OUTER JOIN " +
				"historiaclinica.superficie_dental sd " +
					"ON(sd.codigo=oh.superficie) " +
			"INNER JOIN " +
				"odontologia.hallazgos_odontologicos hodo " +
					"ON(hodo.consecutivo=oh.hallazgo) " +
			"LEFT JOIN " +
				"odontologia.convenciones_odontologicas codo " +
							"ON(codo.consecutivo=hodo.convencion) " +
			"WHERE " +
					"oh.odontograma=? " +
				"AND " +
					"oh.pieza_dental IS NULL "+
				"AND " +
					"hodo.aplica_a = '"+ConstantesIntegridadDominio.acronimoAplicaABoca+"'";;
			return llenarArrayListDtos(sentenciaConsulta, con, codigoOdontograma);
		}
		return null;
	}

	private static ArrayList<DtoOtroHallazgo> llenarArrayListDtos(String sentenciaConsulta,
			Connection con, int codigoOdontograma)
	{
		try
		{
			ArrayList<DtoOtroHallazgo> hallazgos=new ArrayList<DtoOtroHallazgo>();
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentenciaConsulta);
			psd.setInt(1, codigoOdontograma);
			
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			while(rsd.next())
			{
				DtoOtroHallazgo dtoOtroHallazgo=new DtoOtroHallazgo();
				dtoOtroHallazgo.setCodigoPk(rsd.getInt("codigo_pk"));
				
				DtoHallazgoOdontologico dtoHallazgoOdontologico=new DtoHallazgoOdontologico();
				dtoHallazgoOdontologico.setNombre(rsd.getString("nombre_hallazgo"));
				dtoHallazgoOdontologico.setCodigo(rsd.getString("hallazgo"));
				dtoHallazgoOdontologico.setAplica_a(rsd.getString("aplica_a"));
				dtoOtroHallazgo.setHallazgo(dtoHallazgoOdontologico);
				
				DtoPiezaDental dtoPiezaDental=new DtoPiezaDental();
				int pieza=rsd.getInt("pieza_dental");
				if(pieza>0)
				{
					dtoPiezaDental.setCodigoPk(pieza);
					dtoPiezaDental.setNombre(rsd.getString("descripcion_pieza"));
				}
				dtoOtroHallazgo.setPieza(dtoPiezaDental);
				
				DtoSuperficie dtoSuperficie=new DtoSuperficie();
				int superficie=rsd.getInt("superficie");
				if(superficie>0)
				{
					dtoSuperficie.setCodigoPk(superficie);
					dtoSuperficie.setNombre(rsd.getString("nombre_superficie"));
				}
				
				dtoOtroHallazgo.setSuperficie(dtoSuperficie);
				
				dtoOtroHallazgo.setFecha(UtilidadFecha.conversionFormatoFechaAAp(rsd.getString("fecha")));
				dtoOtroHallazgo.setHora(rsd.getString("hora"));
				dtoOtroHallazgo.setArchivoConvencion(rsd.getString("archivoConvencion"));
				dtoOtroHallazgo.setColorBorde(rsd.getString("colorBorde"));
				
				hallazgos.add(dtoOtroHallazgo);
			}
			rsd.close();
			psd.close();
			return hallazgos;
		} catch (SQLException e)
		{
			logger.error("Error consultando los otros hallazgos",e);
		}
		return new ArrayList<DtoOtroHallazgo>();
	}

	
	
	/**
	    * 
	    * @param dtoWhere
	    * @return
	    */
		public static ArrayList<DtoOdontograma> cargar(DtoOdontograma dtoWhere , DtoPlanTratamientoOdo dtoPlan) 
		{
			
			
			
			
			ArrayList<DtoOdontograma> arrayDto = new ArrayList<DtoOdontograma>();
			String consultaStr = 	"SELECT  " +
											" distinct " +
											" odo.codigo_pk as codigo_pk , " +
											" odo.consecutivo as consecutivo , " +
											" odo.codigo_paciente as codigo_paciente ," +
											" odo.ingreso as ingreso ," +
											" ig.estado as estado_ingreso ," +
											" getnombrecentatenxing(ig.id) as nombre_centro_atencion ,"+
											" cu.via_ingreso as via_ingreso ,"+
											" odo.valoracion as valoracion ," +
											" odo.indicativo as indicativo ," +
											" odo.evolucion as evolucion ," +
											" odo.institucion as institucion ," +
											" odo.centro_atencion as centro_atencion ," +
											" odo.imagen as imagen ,"+
											" getnombreusuario(odo.usuario_modifica) as nombre_usuario_modifica ," +
											" odo.usuario_modifica as usuario_modifica ," +
											" odo.fecha_modifica as fecha_modifica ," +
											" odo.hora_modifica as hora_modifica   " +
											
											
										"from " +
											" odontologia.odontograma  odo  " +
											" INNER JOIN odontologia.log_plan_tratamiento p on " +
											"	(p.odontograma_diagnostico=odo.codigo_pk and p.por_confirmar='"+ConstantesBD.acronimoNo+"') " +
											" INNER JOIN manejopaciente.ingresos ig ON(ig.id=odo.ingreso) " +
											" INNER JOIN manejoPaciente.cuentas cu ON(ig.id=cu.id_ingreso) " +

										"where " +
											"1=1 ";
											
										
			consultaStr+= (dtoWhere.getCodigoPk()> 0)?" and odo.codigo_pk= "+dtoWhere.getCodigoPk():"";
			consultaStr+= (dtoWhere.getInstitucion()> 0)?" and odo.institucion= "+dtoWhere.getInstitucion():"";
			consultaStr+= (dtoWhere.getIngreso().getCodigo()> 0)?" and odo.ingreso= "+dtoWhere.getIngreso().getCodigo():"";
			consultaStr+= (dtoWhere.getIngreso().getCodigo2()> 0)?" and cu.via_ingreso= "+dtoWhere.getIngreso().getCodigo2():"";
			consultaStr+=(dtoWhere.getEvolucion()>0)?" and odo.evolucion="+dtoWhere.getEvolucion(): " ";
			if(dtoPlan!=null)
			{
				consultaStr+=dtoPlan.getCodigoEvolucion().doubleValue()>0?" and p.evolucion="+dtoPlan.getCodigoEvolucion(): " ";
			}
			
			consultaStr+=!UtilidadTexto.isEmpty(dtoWhere.getIndicativo())?" and odo.indicativo='"+dtoWhere.getIndicativo()+"' ":" ";
			consultaStr+=" order by odo.ingreso desc"; 
			
			
			logger.info("\n\n\n\n\n\n");
			logger.info(" CONSULTA ODONTOGRAMA ");
			logger.info(""+consultaStr);
			logger.info("\n\n\n\n\n\n");
			try 
			{
				//logger.info("\n\n\n\n\n SQL cargar emison Tar odontologico / " + consultaStr);
				Connection con = UtilidadBD.abrirConexion();
				PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator rs=null;
				rs = new ResultSetDecorator(ps.executeQuery());
				while (rs.next()) 
				{				
					
					DtoOdontograma dto = new DtoOdontograma();
					dto.setCodigoPk(rs.getDouble("codigo_pk"));
					dto.setIngreso(new InfoDatosInt(rs.getInt("ingreso"),ValoresPorDefecto.getIntegridadDominio(rs.getString("estado_ingreso")).toString()));
					dto.setConsecutivo(rs.getDouble("consecutivo"));
					dto.setValoracion(rs.getInt("valoracion"));
					dto.setIndicativo(rs.getString("indicativo"));
					dto.setEvolucion(rs.getDouble("evolucion"));
					dto.setInstitucion(rs.getInt("institucion"));
					dto.setImagen(rs.getString("imagen"));
					dto.setCentroAtencion(new InfoDatosInt(rs.getInt("centro_atencion"), rs.getString("nombre_centro_atencion")));			
					dto.getUsuarioModifica().setFechaModifica(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecha_modifica")));
					dto.getUsuarioModifica().setHoraModifica(rs.getString("hora_modifica"));
					dto.getUsuarioModifica().setUsuarioModifica(rs.getString("usuario_modifica"));
					dto.getUsuarioModifica().setNombreUsuarioModifica(rs.getString("nombre_usuario_modifica"));
					
					
					arrayDto.add(dto);
				}
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			} 
			catch (SQLException e) 
			{
				logger.error("error en carga Odontograma==> " + e);
			}
			return arrayDto;
		}
		
		

	    /**
		 * CARGAR INGRESO
		 * @param dtoWhere
		 * @return
		 */
	    public static ArrayList<DtoOtrosIngresosPaciente> cargarIngresos(int paciente , int viaIngreso , int institucion) 
		{
			
			
			ArrayList<DtoOtrosIngresosPaciente> arrayDto = new ArrayList<DtoOtrosIngresosPaciente>();
			String consultaStr = 	"SELECT  " +
											
											"distinct ig.id as ingreso ," +
											"ig.consecutivo as ingreso_consecutivo ," +
											"ig.estado as estado_ingreso ," +
											"getnombrecentatenxing(ig.id) as nombre_centro_atencion ,"+
											"cu.via_ingreso as via_ingreso ,"+								
											
											"ig.fecha_ingreso as fecha_ingreso ," +
											"ig.hora_ingreso as hora_ingreso " +
											
											
											
										"from " +
											"manejopaciente.ingresos  ig  " +
											"INNER JOIN odontologia.odontograma odo ON(ig.id=odo.ingreso) " +
											"INNER JOIN manejoPaciente.cuentas cu ON(ig.id=cu.id_ingreso) " +

										"where " +
											"1=1 ";
											
										
			
			consultaStr+= (institucion > 0)?" and ig.institucion= "+institucion:"";
			consultaStr+= (institucion > 0)?" and ig.codigo_paciente= "+paciente:""; 
			consultaStr+= (viaIngreso > 0)?" and cu.via_ingreso= "+viaIngreso:"";
			
			
			
			
			try 
			{
				logger.info("\n\n\n\n\n SQL cargar emison Tar odontologico / " + consultaStr);
				Connection con = UtilidadBD.abrirConexion();
				PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator rs=null;
				rs = new ResultSetDecorator(ps.executeQuery());
				while (rs.next()) 
				{
					
					
					
					DtoOtrosIngresosPaciente dto = new DtoOtrosIngresosPaciente();
					dto.setIngreso(String.valueOf(rs.getInt("ingreso")));
					dto.setEstadoIngreso(rs.getString("estado_ingreso"));
					dto.setCentroAtencion(rs.getString("nombre_centro_atencion"));
					dto.setFechaIngreso(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecha_ingreso")));
					dto.setHoraIngreso(rs.getString("hora_ingreso"));
					dto.setIngresoConsecutivo(String.valueOf(rs.getInt("ingreso_consecutivo")));
					arrayDto.add(dto);
				}
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			} 
			catch (SQLException e) 
			{
				logger.error("error en carga Odontograma==> " + e);
			}
			return arrayDto;
		}
	    
	    

		
		public static String retornarConsultaOdontograma(double codigoOdontograma , String tipoOdontograma , int ingreso , String seccion ,boolean inclusion , boolean garantia)
		{
			
			/*
			logger.info("\n\n\n\n\n");
			logger.info(" 	RETORNAR CONSULTA ODONTONTOGRAMA \n\n\n");
			logger.info("EL TIPO DE ODONTOGRAMA ES **************************************************************************************************************"+tipoOdontograma);
					
			              String consulta = "select " +
							"dpt.pieza_dental as pieza_dental, " +
							"dpt.superficie as superficie, " +
							"( " +
								"select " +
									"historiaclinica.getNombreSuperficie(su.sector, dpt.pieza_dental ) " +
								"from " +
									"historiaclinica.superficie_dental su " +
								"where " +
									"su.codigo=dpt.superficie " +
							") as nombre_superficie, " +
							"(" +
								"select " +
									"ho.nombre " +
								"from " +
									"odontologia.hallazgos_odontologicos ho " +
								"where " +
									"ho.consecutivo=dpt.hallazgo" +
							") as nombre_hallazgo, " +
							"dpt.hallazgo as hallazgo, " +
							"dpt.seccion as seccion, " +
							"getintegridaddominio(dpt.clasificacion) as clasificacion, " +
							"coalesce(pst.programa,pst.servicio) as programa, " +
							"getintegridaddominio(pst.estado_servicio)  as estado, " +
							"getintegridaddominio(pst.estado_programa)  as estado_programa, " +
							"coalesce(" +
										"(" +
											"select " +
												"pro.nombre " +
											"from " +
												"odontologia.programas pro " +
											"where " +
												"pro.codigo  = pst.programa" +
										"), " +
										"getnombreservicio(pst.servicio,0)" +
									") as nombre_programa, " +
									"getnombreservicio(pst.servicio,0) as servicio, " +
									"case when (pst.exclusion = '"+ConstantesBD.acronimoSi+"') then 'exclusion' else (case when pst.garantia = '"+ConstantesBD.acronimoSi+"' then 'Garantia' else '' end) END as eg, " +
									"case when (ci.estado_cita ='"+ConstantesIntegridadDominio.acronimoProgramado+"') then ci.fecha_cita ||'' else (case when (co.estado ='"+ConstantesIntegridadDominio.acronimoProgramado+"') then co.fecha_programacion||'' else '' END) END as fecha_cita " +
									"from odontologia.his_conf_det_plan_t dpt " ;
				
		      if(tipoOdontograma.equals(ConstantesIntegridadDominio.acronimoOdontogramaTratamiento))
		      {
		    	  consulta+=" INNER JOIN odontologia.his_conf_plan_tratamiento pt on (dpt.plan_tratamiento = pt.codigo_pk and pt.ingreso="+ingreso+"  and pt.odontograma_evolucion = "+codigoOdontograma+" and pt.codigo_pk= (select max(pt2.codigo_pk)  as codigo_pk from odontologia.his_conf_plan_tratamiento pt2 where pt2.plan_tratamiento = dpt.plan_tratamiento)) " ;
		      }else
		      {
		    	  consulta+=" INNER JOIN odontologia.his_conf_plan_tratamiento pt on (dpt.plan_tratamiento = pt.codigo_pk and pt.ingreso="+ingreso+" and pt.odontograma_diagnostico = "+codigoOdontograma+" and pt.codigo_pk= (select max(pt2.codigo_pk)  as codigo_pk from odontologia.his_conf_plan_tratamiento pt2 where pt2.plan_tratamiento = dpt.plan_tratamiento) ) " ;
		      }
		      consulta+=" INNER JOIN odontologia.his_conf_prog_serv_plan_t pst on (pst.det_plan_tratamiento = dpt.codigo_pk  and pst.codigo_pk= (select max(pst2.codigo_pk)  as codigo_pk from odontologia.his_conf_prog_serv_plan_t pst2 where pst2.det_plan_tratamiento = dpt.codigo_pk))" +
		
		
		      "			 LEFT OUTER JOIN odontologia.servicios_cita_odontologica ci on (ci.programa_servicio_plan_t = pst.codigo_pk ) " +
		      "			 LEFT OUTER JOIN odontologia.citas_odontologicas co on (ci.cita_odontologica  = co.codigo_pk ) " +
		
		      "  where  pt.ingreso ="+ingreso+" " ;
		      if(!UtilidadTexto.isEmpty(seccion))
		      {
		    	  consulta+=		" and dpt.seccion = '"+seccion+"'" ;
		      }
		
		
		
		      if(tipoOdontograma.equals(ConstantesIntegridadDominio.acronimoOdontogramaTratamiento))
		      {
		    	  if(!inclusion && !garantia)
		    	  {
		
		    		  consulta+=" and (pst.inclusion='"+ConstantesBD.acronimoNo+"' or pst.inclusion is null ) " ;
		    	  }
		    	  else
		    	  {
		    		  if(garantia)
		    		  {
		
		    			  consulta+=" and (pst.garantia='"+ConstantesBD.acronimoSi+"'  )" ;
		
		    		  } else if (inclusion)
		    		  {
		    			  consulta+=" and (pst.inclusion='"+ConstantesBD.acronimoSi+"'  ) ";
		    		  }
		    	  }
		      }
		
		      consulta+=" and dpt.codigo_pk = (select max(dpt2.codigo_pk)  as codigo_pk from odontologia.his_conf_det_plan_t dpt2 where dpt2.plan_tratamiento = pt.codigo_pk) ";
		      consulta+=";";
		*/
			
			String consulta="";
			
			consulta="select "+ 
				" dpt.pieza_dental as pieza_dental, "+ 
				" dpt.superficie as superficie,  "+
				" ( select historiaclinica.getNombreSuperficie(su.sector, dpt.pieza_dental ) from historiaclinica.superficie_dental su where su.codigo=dpt.superficie ) as nombre_superficie, "+ 
				" (select ho.nombre from odontologia.hallazgos_odontologicos ho where ho.consecutivo=dpt.hallazgo) as nombre_hallazgo, "+
				" dpt.hallazgo as hallazgo , "+ 
				" dpt.seccion as seccion,  "+
				" getintegridaddominio(dpt.clasificacion) as clasificacion, "+ 
				" coalesce(pst.programa,pst.servicio) as programa, "+
				" getintegridaddominio(pst.estado_servicio)  as estado,"+ 
				" getintegridaddominio(pst.estado_programa)  as estado_programa,"+  
				" coalesce((select pro.nombre from odontologia.programas pro where pro.codigo  = pst.programa),"+ 
				" getnombreservicio(pst.servicio,0)) as nombre_programa, "+
				" getnombreservicio(pst.servicio,0) as servicio, "+
				" case when (pst.exclusion = 'S') then 'exclusion' else ( case when pst.garantia = 'S' then 'Garantia' else '' end ) END as eg ," +
				
			
				" from " +
					" odontologia.plan_tratamiento p " +
					"	inner join " +
					"odontologia.det_plan_tratamiento dpt " +
					"		on (p.codigo_pk=dpt.plan_tratamiento and p.codigo_pk=1054 " +
					"		and p.ingreso=1260 and  dpt.activo='S' and dpt.seccion='DET') " +
					"	left outer join " +
					"odontologia.programas_servicios_plan_t pst on(pst.det_plan_tratamiento=dpt.codigo_pk) "; 
			
			
		    return consulta;
		}
		
		
		/**
		 * Retorna la Consulta SQl Para cargar el Plan de Tratamiento
		 * @param dtoPlanTratamiento
		 * @param seccion
		 * @param codigoTarifario TODO
		 * @param ingreso
		 * @return
		 */
		public static String retornarConsultaOdontogramaPlanTratamiento(DtoPlanTratamientoOdo dtoPlanTratamiento , String seccion, String tiposInclusionGarantia, int codigoTarifario)
		{
			/*
			 * ARMANDO CONSULTA
			 */
			
			
			String consulta="select " +
				" pst.servicio as codigo_servicio ," +
				" pst.programa as codigo_programa,"+ 
				" dpt.codigo_pk AS pk_detalle, " +
				" dpt.pieza_dental as pieza_dental, "+ 
				" dpt.superficie as superficie,  "+
				" coalesce(getNombreSuperficie(ssc.sector, dpt.pieza_dental), '') as nombre_superficie , "+  
				" (select ho.nombre from odontologia.hallazgos_odontologicos ho where ho.consecutivo=dpt.hallazgo) as nombre_hallazgo, "+
				" dpt.hallazgo as hallazgo , "+ 
				" dpt.seccion as seccion,  "+
				" getintegridaddominio(dpt.clasificacion) as clasificacion, "; 
			
			
			consulta+=
				
				" coalesce(pst.programa,pst.servicio) as programa, "+
				" getintegridaddominio(pst.estado_servicio)  as estado,"+ 
				" getintegridaddominio(pst.estado_programa)  as estado_programa,"+  
				" coalesce((select pro.nombre from odontologia.programas pro where pro.codigo  = pst.programa),"+ 
				" getnombreservicio(pst.servicio,"+codigoTarifario+")) as nombre_programa, "+
				" getnombreservicio(pst.servicio,"+codigoTarifario+") as servicio, "+
				" case when (pst.exclusion = '"+ConstantesBD.acronimoSi+"') then 'Exclusion' else ( case when pst.garantia = '"+ConstantesBD.acronimoSi+"' then 'Garantia' else '' end ) END as eg , "+
				" p.fecha_modifica as  fecha_cita "+ //TODO CAMBIAR LA FECHA PRUEBA
				" from " +
					" odontologia.plan_tratamiento p " +
					"	inner join " +
					"odontologia.det_plan_tratamiento dpt " +
					"	on (p.codigo_pk=dpt.plan_tratamiento and p.codigo_pk="+dtoPlanTratamiento.getCodigoPk() ;
			
			consulta+=" and p.ingreso="+dtoPlanTratamiento.getIngreso()+" and  dpt.activo='"+ConstantesBD.acronimoSi+"' ) "; // TODO CAMBIAR
			consulta+="	INNER JOIN odontologia.programas_servicios_plan_t  pst  on ( pst.det_plan_tratamiento=dpt.codigo_pk)  " +
					" INNER JOIN  odontologia.hallazgos_odontologicos h ON(h.consecutivo=dpt.hallazgo) "+ 
					" LEFT OUTER JOIN  "+
					" historiaclinica.superficie_dental s ON(s.codigo=dpt.superficie) LEFT OUTER JOIN odontologia.sector_superficie_cuadrante ssc ON(ssc.superficie=s.codigo AND dpt.pieza_dental=ssc.pieza)    " +
					" where 1=1 ";
			
			
			
			/*
			 * CLASIFICIACION DE LA SECCION
			 */
			if(seccion!=null)
			{
				// SI LA SECCION ES OTRO NO SE PRESENTA LAS INCLUSIONES
				if(seccion.equals(ConstantesIntegridadDominio.acronimoOtro))
				{
					consulta+=" and  dpt.seccion='"+seccion+"' and (pst.inclusion not in ('"+ConstantesBD.acronimoSi+"') or pst.inclusion is null )";
				}
				else
				{
					consulta+=" and  dpt.seccion='"+seccion+"'";
				}
			}
			
			
			
			/*
			 *CLASIFICACION DE TIPOS
			 */
			if (tiposInclusionGarantia!=null)
			{
				if(tiposInclusionGarantia.equals(ConstantesIntegridadDominio.acronimoInclusion))
				{
					consulta+=" and pst.inclusion='"+ConstantesBD.acronimoSi+"'";
				}
				else if(tiposInclusionGarantia.equals(ConstantesIntegridadDominio.acronimoGarantia))
				{
					consulta+=" and pst.garantia='"+ConstantesBD.acronimoSi+"'";
				}
			}

			consulta+= " order by dpt.pieza_dental desc ";
			 
			return consulta;
		}
		
		
		/**
		 * Retorna la Consulta SQl Para cargar el Plan de Tratamiento
		 * @param dtoPlanTratamiento
		 * @param seccion
		 * @param ingreso
		 * @return
		 */
		public static String retornarConsultaOdontograma(DtoPlanTratamientoOdo dtoPlanTratamiento , String seccion, String tiposInclusionGarantia)
		{
			
			
			/*
			 * ARMADO DEL CONSULTA
			 */
			
			
			String consulta="select " +
				" pst.servicio as codigo_servicio ," +
				" pst.programa as codigo_programa,"+ 
				" dpt.codigo_pk AS pk_detalle, " +
				" dpt.pieza_dental as pieza_dental, "+ 
				" dpt.superficie as superficie,  "+
				" coalesce(getNombreSuperficie(ssc.sector, dpt.pieza_dental), '') as nombre_superficie , "+ 
				" (select ho.nombre from odontologia.hallazgos_odontologicos ho where ho.consecutivo=dpt.hallazgo) as nombre_hallazgo, "+
				" dpt.hallazgo as hallazgo , "+ 
				" dpt.seccion as seccion,  "+
				" getintegridaddominio(dpt.clasificacion) as clasificacion, "; 
			
			
			consulta+=
				
				" coalesce(pst.programa,pst.servicio) as programa, "+
				" getintegridaddominio(pst.estado_servicio)  as estado,"+ 
				" getintegridaddominio(pst.estado_programa)  as estado_programa,"+  
				" coalesce((select pro.nombre from odontologia.programas pro where pro.codigo  = pst.programa),"+ 
				" getnombreservicio(pst.servicio,0)) as nombre_programa, "+
				" getnombreservicio(pst.servicio,0) as servicio, "+
				" case when (pst.exclusion = 'S') then 'exclusion' else ( case when pst.garantia = 'S' then 'Garantia' else '' end ) END as eg , "+
				" p.fecha as  fecha_cita "+ //TODO CAMBIAR LA FECHA PRUEBA
				" from " +
					" odontologia.log_plan_tratamiento p " +
					"	inner join " +
					"odontologia.log_det_plan_tratamiento dpt " +
					"	on (p.plan_tratamiento=dpt.plan_tratamiento and p.plan_tratamiento="+dtoPlanTratamiento.getCodigoPlanHistorico() ;
			
			consulta+=" and p.ingreso="+dtoPlanTratamiento.getIngreso()+" and  dpt.activo='"+ConstantesBD.acronimoSi+"' ) "; 
			consulta+="	 left outer join odontologia.log_programas_servicios_plan_t  pst  on ( pst.det_plan_tratamiento=dpt.det_plan_tratamiento)" +
					" INNER JOIN  odontologia.hallazgos_odontologicos h ON(h.consecutivo=dpt.hallazgo) "+ 
					" LEFT OUTER JOIN  "+
					" historiaclinica.superficie_dental s ON(s.codigo=dpt.superficie) LEFT OUTER JOIN odontologia.sector_superficie_cuadrante ssc ON(ssc.superficie=s.codigo AND dpt.pieza_dental=ssc.pieza)    " +
					" where 1=1 ";
			
			//POR CONFIRMAR NO
			consulta+=" and pst.por_confirmar='"+ConstantesBD.acronimoNo+"' " +
					" AND dpt.por_confirmar='"+ConstantesBD.acronimoNo+"'";
			
			
			
			
			
			/*
			 * CLASIFICIACION DE LA SECCION
			 */
			if(seccion!=null)
			{
				// SI LA SECCION ES OTRO NO SE PRESENTA LAS INCLUSIONES
				if(seccion.equals(ConstantesIntegridadDominio.acronimoOtro))
				{
					consulta+=" and  dpt.seccion='"+seccion+"' and (pst.inclusion not in ('"+ConstantesBD.acronimoSi+"') or pst.inclusion is null )";
				}
				else
				{
					consulta+=" and  dpt.seccion='"+seccion+"'";
				}
			}
			
			
			
			/*
			 *CLASIFICACION DE TIPOS
			 */
			if (tiposInclusionGarantia!=null)
			{
				if(tiposInclusionGarantia.equals(ConstantesIntegridadDominio.acronimoInclusion))
				{
					consulta+=" and pst.inclusion='"+ConstantesBD.acronimoSi+"'";
				}
				else if(tiposInclusionGarantia.equals(ConstantesIntegridadDominio.acronimoGarantia))
				{
					consulta+=" and pst.garantia='"+ConstantesBD.acronimoSi+"'";
				}
			}

			consulta+= " order by dpt.pieza_dental desc ";
			 
			return consulta;
		}
		
		

		
		/**
		* METODO QUE CARGA EL ODONTOGRAMA COMPLETO 
		* 
		* @param esValoracion
		* @param valoracion
		* */
		public static DtoOdontograma cargarOdontograma(DtoOdontograma dtoOdontograma)
		{
			DtoOdontograma dto=new DtoOdontograma();
			try
			{
				Connection con=UtilidadBD.abrirConexion();
				
				String cargarOdontogramaStr = "SELECT " +
							"o.codigo_pk AS codigo_pk, " +
							"o.consecutivo AS consecutivo, " +
							"o.codigo_paciente As codigo_paciente, " +
							"o.ingreso As ingreso, " +
							"o.valoracion AS valoracion, " +
							"o.indicativo AS indicativo, " +
							"o.evolucion AS evolucion, " +
							"o.institucion AS institucion, " +
							"o.centro_atencion AS centro_atencion, " +
							"o.usuario_modifica AS usuario_modifica, " +
							"o.fecha_modifica AS fecha_modifica, " +
							"o.hora_modifica AS hora_modifica, " +
							"o.imagen AS imagen " +
						"FROM odontologia.odontograma o " +
						"WHERE  1=1";
				
				/*
				 * SEGURIDAD
				 */
				if(dtoOdontograma.getCodigoPk()<=0)
				{
					return new DtoOdontograma();
				}
				
				
				//FILTRO ODONTOGRAMA
				cargarOdontogramaStr+=" AND  o.codigo_pk ="+dtoOdontograma.getCodigoPk();
				cargarOdontogramaStr+= dtoOdontograma.getIngreso().getCodigo()>0?" AND o.ingreso="+dto.getIngreso().getCodigo(): " " ;
				cargarOdontogramaStr+=dtoOdontograma.getValoracion()>0? " AND o.valoracion="+dto.getValoracion(): " ";
				cargarOdontogramaStr+=dtoOdontograma.getEvolucion().doubleValue()>0?" AND o.evolucion="+dto.getEvolucion(): " ";
				
				//DRIVER 
				PreparedStatementDecorator ps = new PreparedStatementDecorator(con,cargarOdontogramaStr);
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				logger.info("CONSULTA ODONTOGRAMA "+ps);
				
				
				if(rs.next())
				{
					
					dto.setConsecutivo(rs.getDouble("consecutivo"));
					dto.setCodigoPaciente(rs.getInt("codigo_paciente"));
					dto.setIngreso(new InfoDatosInt(rs.getInt("ingreso")));
					dto.setValoracion(rs.getInt("valoracion"));
					dto.setIndicativo(rs.getString("indicativo"));
					dto.setEvolucion(rs.getDouble("evolucion"));
					dto.setInstitucion(rs.getInt("institucion"));
					dto.setCentroAtencion(new InfoDatosInt(rs.getInt("centro_atencion")));
					dto.setUsuarioModifica(new DtoInfoFechaUsuario(rs.getString("hora_modifica"), rs.getString("fecha_modifica"), rs.getString("usuario_modifica")));
					dto.setImagen(rs.getString("imagen"));
				}			
				ps.close();
				rs.close();
				UtilidadBD.cerrarConexion(con);
			}
			catch (SQLException  e) 
			{
				logger.info("Error al cargar Odontograma "+e );
			}
			
			return dto; // retorna Dto Odontograma
		}
		
		
		
		
		/**
		* METODO QUE CARGA EL ULTIMO ODONTOGRAMA GENERADO EN LOG DEL PLAN TRATAMIENTO
		* RECIBE UN 
		* @param esValoracion
		* @param valoracion
		* */
		public static DtoOdontograma cargarOdontogramaImagen(DtoPlanTratamientoOdo dtoPlan)
		{
			DtoOdontograma dto=new DtoOdontograma();
			try
			{
				Connection con=UtilidadBD.abrirConexion();
				
				String consulta=
					 		" select 	" +
				 			" o.codigo_pk AS codigo_pk, " +
							" o.consecutivo AS consecutivo, " +
							" o.codigo_paciente As codigo_paciente, " +
							" o.ingreso As ingreso, " +
							" o.valoracion AS valoracion, " +
							" o.indicativo AS indicativo, " +
							" o.evolucion AS evolucion, " +
							" o.institucion AS institucion, " +
							" o.centro_atencion AS centro_atencion, " +
							" o.usuario_modifica AS usuario_modifica, " +
							" o.fecha_modifica AS fecha_modifica, " +
							" o.hora_modifica AS hora_modifica, " +
							" o.imagen AS imagen from " +
				 			" odontograma o where " +
				 			" o.codigo_pk= (select max(lp.odontograma_diagnostico) from odontologia.log_plan_tratamiento lp  where lp.plan_tratamiento=?  and lp.ingreso=? )" +
				 			" and ( o.imagen is not null AND o.imagen !='')";
				
				/*
				 * SEGURIDAD
				 */
				if(dtoPlan.getCodigoPk().doubleValue() <=0)
				{
					return new DtoOdontograma();
				}
				
				//DRIVER 
				PreparedStatementDecorator ps = new PreparedStatementDecorator(con,consulta);
				
				ps.setBigDecimal(1, dtoPlan.getCodigoPk() );
				ps.setInt(2, dtoPlan.getIngreso());
				
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				logger.info("CONSULTA ODONTOGRAMA "+ps);
				
				
				if(rs.next())
				{
					
					dto.setConsecutivo(rs.getDouble("consecutivo"));
					dto.setCodigoPaciente(rs.getInt("codigo_paciente"));
					dto.setIngreso(new InfoDatosInt(rs.getInt("ingreso")));
					dto.setValoracion(rs.getInt("valoracion"));
					dto.setIndicativo(rs.getString("indicativo"));
					dto.setEvolucion(rs.getDouble("evolucion"));
					dto.setInstitucion(rs.getInt("institucion"));
					dto.setCentroAtencion(new InfoDatosInt(rs.getInt("centro_atencion")));
					dto.setUsuarioModifica(new DtoInfoFechaUsuario(rs.getString("hora_modifica"), rs.getString("fecha_modifica"), rs.getString("usuario_modifica")));
					dto.setImagen(rs.getString("imagen"));
				}			
				ps.close();
				rs.close();
				UtilidadBD.cerrarConexion(con);
			}
			catch (SQLException  e) 
			{
				logger.info("Error al cargar Odontograma "+e );
			}
			
			return dto; // retorna Dto Odontograma
		}
		

			
		
	
}
