package com.sysmedica.mundo.saludpublica;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import util.UtilidadFecha;

import com.princetonsa.dao.DaoFactory;
import com.sysmedica.dao.FichaBrotesDao;

public class FichaBrotes {

	Logger logger=Logger.getLogger(FichaBrotes.class);
	private String sire;
	private int evento;
	private String fechaNotificacion;
	private String paisNotifica;
	private int departamentoNotifica;
	private int ciudadNotifica;
	private String lugarNotifica;
	private int unidadGeneradora;
	
	private int pacientesGrupo1;
	private int pacientesGrupo2;
	private int pacientesGrupo3;
	private int pacientesGrupo4;
	private int pacientesGrupo5;
	private int pacientesGrupo6;
	private int probables;
	private int confirmadosLaboratorio;
	private int confirmadosClinica;
	private int confirmadosNexo;
	private int hombres;
	private int mujeres;
	private int vivos;
	private int muertos;
	private int departamentoProcedencia;
	private int ciudadProcedencia;
	private String lugarProcedencia;
	private String nombreProfesional;
	private String telefonoContacto;
	
	private int muestraBiologica;
	private String agenteEtiologico1;
	private String alimentosImplicados;
	private int muestraAlimentos;
	private String agenteEtiologico2;
	private String lugarConsumo;
	
	private int estadoFicha;
	private String loginUsuario;
	private int codigoFichaBrotes;
	private String observaciones;
	
	FichaBrotesDao fichaBrotesDao;
	
	private String fechaInvestigacion; 
	private int muestraSuperficies;
	private int estudioManipuladores; 
	private int agenteBiologica1; 
	private int agenteBiologica2; 
	private int agenteBiologica3; 
	private int agenteBiologica4; 
	private int agenteAlimentos1; 
	private int agenteAlimentos2; 
	private int agenteAlimentos3; 
	private int agenteAlimentos4; 
	private int agenteSuperficies1; 
	private int agenteSuperficies2; 
	private int agenteSuperficies3; 
	private int agenteSuperficies4; 
	private int agenteManipuladores1;
	private int agenteManipuladores2;
	private int agenteManipuladores3;
	private int agenteManipuladores4;
	private int lugarConsumoImplicado;
	private int factorDeterminante;
	private int medidaSanitaria;
	
	
	/**
     * Inicializa el acceso a Base de Datos de este objeto
     * @param tipoBD
     * @return
     */
    public boolean init(String tipoBD)
	{
			boolean wasInited = false;
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			if (myFactory != null)
			{
				fichaBrotesDao = myFactory.getFichaBrotesDao();
				wasInited = (fichaBrotesDao != null);
			}
			return wasInited;
	}
    
    
    public void reset() {
    	
    	sire = "";
		evento = 0;
		fechaNotificacion = "";
		paisNotifica = "";
		lugarNotifica = "";
		unidadGeneradora = 0;
		
		pacientesGrupo1 = 0;
		pacientesGrupo2 = 0;
		pacientesGrupo3 = 0;
		pacientesGrupo4 = 0;
		pacientesGrupo5 = 0;
		pacientesGrupo6 = 0;
		probables = 0;
		confirmadosLaboratorio = 0;
		confirmadosClinica = 0;
		confirmadosNexo = 0;
		hombres = 0;
		mujeres = 0;
		vivos = 0;
		muertos = 0;
		departamentoProcedencia = 0;
		ciudadProcedencia = 0;
		lugarProcedencia = "";
		nombreProfesional = "";
		telefonoContacto = "";
		
		muestraBiologica = 0;
		agenteEtiologico1 = "";
		alimentosImplicados = "";
		muestraAlimentos = 0;
		agenteEtiologico2 = "";
		lugarConsumo = "";
    }
    
    
    public FichaBrotes()
    {
    	reset();
		init(System.getProperty("TIPOBD"));
    }
    
    
    public int insertarFicha(Connection con) 
    {
    	return fichaBrotesDao.insertarFicha(con,
												loginUsuario,
												estadoFicha,
											    
											    sire,
												loginUsuario,
											    codigoFichaBrotes,
											    
											    evento,
												fechaNotificacion,
												paisNotifica,
												departamentoNotifica,
												ciudadNotifica,
												lugarNotifica,
												unidadGeneradora,
												
												pacientesGrupo1,
												pacientesGrupo2,
												pacientesGrupo3,
												pacientesGrupo4,
												pacientesGrupo5,
												pacientesGrupo6,
												probables,
												confirmadosLaboratorio,
												confirmadosClinica,
												confirmadosNexo,
												hombres,
												mujeres,
												vivos,
												muertos,
												departamentoProcedencia,
												ciudadProcedencia,
												lugarProcedencia,
												nombreProfesional,
												telefonoContacto,
												
												muestraBiologica,
												agenteEtiologico1,
												alimentosImplicados,
												muestraAlimentos,
												agenteEtiologico2,
												lugarConsumo,
												observaciones,
												
												fechaInvestigacion, 
												muestraSuperficies,
												estudioManipuladores, 
												agenteBiologica1, 
												agenteBiologica2, 
												agenteBiologica3, 
												agenteBiologica4, 
												agenteAlimentos1, 
												agenteAlimentos2, 
												agenteAlimentos3, 
												agenteAlimentos4, 
												agenteSuperficies1, 
												agenteSuperficies2, 
												agenteSuperficies3, 
												agenteSuperficies4, 
												agenteManipuladores1,
												agenteManipuladores2,
												agenteManipuladores3,
												agenteManipuladores4,
												lugarConsumoImplicado,
												factorDeterminante,
												medidaSanitaria);
    }
    
    
    public int modificarFicha(Connection con)
    {
    	return fichaBrotesDao.modificarFicha(con,
												sire,
												loginUsuario,
											    codigoFichaBrotes,
											    estadoFicha,
											    
											    evento,
												fechaNotificacion,
												paisNotifica,
												departamentoNotifica,
												ciudadNotifica,
												lugarNotifica,
												unidadGeneradora,
												
												pacientesGrupo1,
												pacientesGrupo2,
												pacientesGrupo3,
												pacientesGrupo4,
												pacientesGrupo5,
												pacientesGrupo6,
												probables,
												confirmadosLaboratorio,
												confirmadosClinica,
												confirmadosNexo,
												hombres,
												mujeres,
												vivos,
												muertos,
												departamentoProcedencia,
												ciudadProcedencia,
												lugarProcedencia,
												nombreProfesional,
												telefonoContacto,
												
												muestraBiologica,
												agenteEtiologico1,
												alimentosImplicados,
												muestraAlimentos,
												agenteEtiologico2,
												lugarConsumo,
												observaciones,
												fechaInvestigacion, 
												muestraSuperficies,
												estudioManipuladores, 
												agenteBiologica1, 
												agenteBiologica2, 
												agenteBiologica3, 
												agenteBiologica4, 
												agenteAlimentos1, 
												agenteAlimentos2, 
												agenteAlimentos3, 
												agenteAlimentos4, 
												agenteSuperficies1, 
												agenteSuperficies2, 
												agenteSuperficies3, 
												agenteSuperficies4, 
												agenteManipuladores1,
												agenteManipuladores2,
												agenteManipuladores3,
												agenteManipuladores4,
												lugarConsumoImplicado,
												factorDeterminante,
												medidaSanitaria);
    }
    
    
    
    public void cargarDatos(Connection con, int codigo)
    {
    	this.codigoFichaBrotes = codigo;
    	
    	ResultSet rs = fichaBrotesDao.consultaTodoFicha(con,codigo);
    	    	
    	try {
    		if (rs.next()) {
    			
    			this.setSire(rs.getString("sire"));
    			this.setEvento(rs.getInt("evento"));
    			this.setFechaNotificacion(rs.getString("fechanotificacion"));
    			this.setPaisNotifica(rs.getString("paisnotifica"));
    			this.setDepartamentoNotifica(Integer.parseInt(rs.getString("departamentonotifica")));
    			this.setCiudadNotifica(Integer.parseInt(rs.getString("municipionotifica")));
    			this.setUnidadGeneradora(rs.getInt("unidadgeneradora"));
    			this.setPacientesGrupo1(rs.getInt("pacientesgrupo1"));
    			this.setPacientesGrupo2(rs.getInt("pacientesgrupo2"));
    			this.setPacientesGrupo3(rs.getInt("pacientesgrupo3"));
    			this.setPacientesGrupo4(rs.getInt("pacientesgrupo4"));
    			this.setPacientesGrupo5(rs.getInt("pacientesgrupo5"));
    			this.setPacientesGrupo6(rs.getInt("pacientesgrupo6"));
    			this.setProbables(rs.getInt("probables"));
    			this.setConfirmadosLaboratorio(rs.getInt("confirmadoslaboratorio"));
    			this.setConfirmadosClinica(rs.getInt("confirmadosclinica"));
    			this.setConfirmadosNexo(rs.getInt("confirmadosnexo"));
    			this.setHombres(rs.getInt("hombres"));
    			this.setMujeres(rs.getInt("mujeres"));
    			this.setVivos(rs.getInt("vivos"));
    			this.setMuertos(rs.getInt("muertos"));
    			this.setDepartamentoProcedencia(Integer.parseInt(rs.getString("departamentoprocedencia")));
    			this.setCiudadProcedencia(Integer.parseInt(rs.getString("municipioprocedencia")));
    			this.setNombreProfesional(rs.getString("nombreprofesionaldiligencio"));
    			this.setTelefonoContacto(rs.getString("telefonocontacto"));
    			this.setMuestraBiologica(rs.getInt("muestrabiologica"));
    			this.setAgenteEtiologico1(rs.getString("agenteetiologico1"));
    			this.setAlimentosImplicados(rs.getString("alimentos"));
    			this.setMuestraAlimentos(rs.getInt("muestraalimentos"));
    			this.setAgenteEtiologico2(rs.getString("agenteetiologico2"));
    			this.setLugarConsumo(rs.getString("lugarconsumo"));
    			this.setObservaciones(rs.getString("observaciones"));
    			
    			this.setFechaInvestigacion(rs.getString("fechaInvestigacion"));
    			this.setMuestraSuperficies(rs.getInt("muestraSuperficies"));
				this.setEstudioManipuladores(rs.getInt("estudioManipuladores")); 
				this.setAgenteBiologica1(rs.getInt("agenteBiologica1")); 
				this.setAgenteBiologica2(rs.getInt("agenteBiologica2")); 
				this.setAgenteBiologica3(rs.getInt("agenteBiologica3")); 
				this.setAgenteBiologica4(rs.getInt("agenteBiologica4")); 
				this.setAgenteAlimentos1(rs.getInt("agenteAlimentos1")); 
				this.setAgenteAlimentos2(rs.getInt("agenteAlimentos2")); 
				this.setAgenteAlimentos3(rs.getInt("agenteAlimentos3")); 
				this.setAgenteAlimentos4(rs.getInt("agenteAlimentos4")); 
				this.setAgenteSuperficies1(rs.getInt("agenteSuperficies1")); 
				this.setAgenteSuperficies2(rs.getInt("agenteSuperficies2")); 
				this.setAgenteSuperficies3(rs.getInt("agenteSuperficies3")); 
				this.setAgenteSuperficies4(rs.getInt("agenteSuperficies4")); 
				this.setAgenteManipuladores1(rs.getInt("agenteManipuladores1"));
				this.setAgenteManipuladores2(rs.getInt("agenteManipuladores2"));
				this.setAgenteManipuladores3(rs.getInt("agenteManipuladores3"));
				this.setAgenteManipuladores4(rs.getInt("agenteManipuladores4"));
				this.setLugarConsumoImplicado(rs.getInt("lugarConsumoImplicado"));
				this.setFactorDeterminante(rs.getInt("factorDeterminante"));
				this.setMedidaSanitaria(rs.getInt("medidaSanitaria"));
    		}
    	}
    	catch (SQLException sqle) {
    		logger.error(sqle.getMessage());
    	}
    }


	public String getAgenteEtiologico1() {
		return agenteEtiologico1;
	}


	public void setAgenteEtiologico1(String agenteEtiologico1) {
		this.agenteEtiologico1 = agenteEtiologico1;
	}


	public String getAgenteEtiologico2() {
		return agenteEtiologico2;
	}


	public void setAgenteEtiologico2(String agenteEtiologico2) {
		this.agenteEtiologico2 = agenteEtiologico2;
	}


	public String getAlimentosImplicados() {
		return alimentosImplicados;
	}


	public void setAlimentosImplicados(String alimentosImplicados) {
		this.alimentosImplicados = alimentosImplicados;
	}


	public int getCiudadNotifica() {
		return ciudadNotifica;
	}


	public void setCiudadNotifica(int ciudadNotifica) {
		this.ciudadNotifica = ciudadNotifica;
	}


	public int getCiudadProcedencia() {
		return ciudadProcedencia;
	}


	public void setCiudadProcedencia(int ciudadProcedencia) {
		this.ciudadProcedencia = ciudadProcedencia;
	}


	public int getConfirmadosClinica() {
		return confirmadosClinica;
	}


	public void setConfirmadosClinica(int confirmadosClinica) {
		this.confirmadosClinica = confirmadosClinica;
	}


	public int getConfirmadosLaboratorio() {
		return confirmadosLaboratorio;
	}


	public void setConfirmadosLaboratorio(int confirmadosLaboratorio) {
		this.confirmadosLaboratorio = confirmadosLaboratorio;
	}


	public int getConfirmadosNexo() {
		return confirmadosNexo;
	}


	public void setConfirmadosNexo(int confirmadosNexo) {
		this.confirmadosNexo = confirmadosNexo;
	}


	public int getDepartamentoNotifica() {
		return departamentoNotifica;
	}


	public void setDepartamentoNotifica(int departamentoNotifica) {
		this.departamentoNotifica = departamentoNotifica;
	}


	public int getDepartamentoProcedencia() {
		return departamentoProcedencia;
	}


	public void setDepartamentoProcedencia(int departamentoProcedencia) {
		this.departamentoProcedencia = departamentoProcedencia;
	}


	public int getEstadoFicha() {
		return estadoFicha;
	}


	public void setEstadoFicha(int estadoFicha) {
		this.estadoFicha = estadoFicha;
	}


	public int getEvento() {
		return evento;
	}


	public void setEvento(int evento) {
		this.evento = evento;
	}


	public String getFechaNotificacion() {
		return fechaNotificacion;
	}


	public void setFechaNotificacion(String fechaNotificacion) {
		this.fechaNotificacion = fechaNotificacion;
	}


	public int getHombres() {
		return hombres;
	}


	public void setHombres(int hombres) {
		this.hombres = hombres;
	}


	public String getLugarConsumo() {
		return lugarConsumo;
	}


	public void setLugarConsumo(String lugarConsumo) {
		this.lugarConsumo = lugarConsumo;
	}


	public String getLugarNotifica() {
		return lugarNotifica;
	}


	public void setLugarNotifica(String lugarNotifica) {
		this.lugarNotifica = lugarNotifica;
	}


	public String getLugarProcedencia() {
		return lugarProcedencia;
	}


	public void setLugarProcedencia(String lugarProcedencia) {
		this.lugarProcedencia = lugarProcedencia;
	}


	public int getMuertos() {
		return muertos;
	}


	public void setMuertos(int muertos) {
		this.muertos = muertos;
	}


	public int getMuestraAlimentos() {
		return muestraAlimentos;
	}


	public void setMuestraAlimentos(int muestraAlimentos) {
		this.muestraAlimentos = muestraAlimentos;
	}


	public int getMuestraBiologica() {
		return muestraBiologica;
	}


	public void setMuestraBiologica(int muestraBiologica) {
		this.muestraBiologica = muestraBiologica;
	}


	public int getMujeres() {
		return mujeres;
	}


	public void setMujeres(int mujeres) {
		this.mujeres = mujeres;
	}


	public String getNombreProfesional() {
		return nombreProfesional;
	}


	public void setNombreProfesional(String nombreProfesional) {
		this.nombreProfesional = nombreProfesional;
	}


	public int getPacientesGrupo1() {
		return pacientesGrupo1;
	}


	public void setPacientesGrupo1(int pacientesGrupo1) {
		this.pacientesGrupo1 = pacientesGrupo1;
	}


	public int getPacientesGrupo2() {
		return pacientesGrupo2;
	}


	public void setPacientesGrupo2(int pacientesGrupo2) {
		this.pacientesGrupo2 = pacientesGrupo2;
	}


	public int getPacientesGrupo3() {
		return pacientesGrupo3;
	}


	public void setPacientesGrupo3(int pacientesGrupo3) {
		this.pacientesGrupo3 = pacientesGrupo3;
	}


	public int getPacientesGrupo4() {
		return pacientesGrupo4;
	}


	public void setPacientesGrupo4(int pacientesGrupo4) {
		this.pacientesGrupo4 = pacientesGrupo4;
	}


	public int getPacientesGrupo5() {
		return pacientesGrupo5;
	}


	public void setPacientesGrupo5(int pacientesGrupo5) {
		this.pacientesGrupo5 = pacientesGrupo5;
	}


	public int getPacientesGrupo6() {
		return pacientesGrupo6;
	}


	public void setPacientesGrupo6(int pacientesGrupo6) {
		this.pacientesGrupo6 = pacientesGrupo6;
	}


	public int getProbables() {
		return probables;
	}


	public void setProbables(int probables) {
		this.probables = probables;
	}


	public String getSire() {
		return sire;
	}


	public void setSire(String sire) {
		this.sire = sire;
	}


	public String getTelefonoContacto() {
		return telefonoContacto;
	}


	public void setTelefonoContacto(String telefonoContacto) {
		this.telefonoContacto = telefonoContacto;
	}


	public int getUnidadGeneradora() {
		return unidadGeneradora;
	}


	public void setUnidadGeneradora(int unidadGeneradora) {
		this.unidadGeneradora = unidadGeneradora;
	}


	public int getVivos() {
		return vivos;
	}


	public void setVivos(int vivos) {
		this.vivos = vivos;
	}


	public String getLoginUsuario() {
		return loginUsuario;
	}


	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}


	public int getCodigoFichaBrotes() {
		return codigoFichaBrotes;
	}


	public void setCodigoFichaBrotes(int codigoFichaBrotes) {
		this.codigoFichaBrotes = codigoFichaBrotes;
	}


	public String getObservaciones() {
		return observaciones;
	}


	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}


	public int getAgenteAlimentos1() {
		return agenteAlimentos1;
	}


	public void setAgenteAlimentos1(int agenteAlimentos1) {
		this.agenteAlimentos1 = agenteAlimentos1;
	}


	public int getAgenteAlimentos2() {
		return agenteAlimentos2;
	}


	public void setAgenteAlimentos2(int agenteAlimentos2) {
		this.agenteAlimentos2 = agenteAlimentos2;
	}


	public int getAgenteAlimentos3() {
		return agenteAlimentos3;
	}


	public void setAgenteAlimentos3(int agenteAlimentos3) {
		this.agenteAlimentos3 = agenteAlimentos3;
	}


	public int getAgenteAlimentos4() {
		return agenteAlimentos4;
	}


	public void setAgenteAlimentos4(int agenteAlimentos4) {
		this.agenteAlimentos4 = agenteAlimentos4;
	}


	public int getAgenteBiologica1() {
		return agenteBiologica1;
	}


	public void setAgenteBiologica1(int agenteBiologica1) {
		this.agenteBiologica1 = agenteBiologica1;
	}


	public int getAgenteBiologica2() {
		return agenteBiologica2;
	}


	public void setAgenteBiologica2(int agenteBiologica2) {
		this.agenteBiologica2 = agenteBiologica2;
	}


	public int getAgenteBiologica3() {
		return agenteBiologica3;
	}


	public void setAgenteBiologica3(int agenteBiologica3) {
		this.agenteBiologica3 = agenteBiologica3;
	}


	public int getAgenteBiologica4() {
		return agenteBiologica4;
	}


	public void setAgenteBiologica4(int agenteBiologica4) {
		this.agenteBiologica4 = agenteBiologica4;
	}


	public int getAgenteManipuladores1() {
		return agenteManipuladores1;
	}


	public void setAgenteManipuladores1(int agenteManipuladores1) {
		this.agenteManipuladores1 = agenteManipuladores1;
	}


	public int getAgenteManipuladores2() {
		return agenteManipuladores2;
	}


	public void setAgenteManipuladores2(int agenteManipuladores2) {
		this.agenteManipuladores2 = agenteManipuladores2;
	}


	public int getAgenteManipuladores3() {
		return agenteManipuladores3;
	}


	public void setAgenteManipuladores3(int agenteManipuladores3) {
		this.agenteManipuladores3 = agenteManipuladores3;
	}


	public int getAgenteManipuladores4() {
		return agenteManipuladores4;
	}


	public void setAgenteManipuladores4(int agenteManipuladores4) {
		this.agenteManipuladores4 = agenteManipuladores4;
	}


	public int getAgenteSuperficies1() {
		return agenteSuperficies1;
	}


	public void setAgenteSuperficies1(int agenteSuperficies1) {
		this.agenteSuperficies1 = agenteSuperficies1;
	}


	public int getAgenteSuperficies2() {
		return agenteSuperficies2;
	}


	public void setAgenteSuperficies2(int agenteSuperficies2) {
		this.agenteSuperficies2 = agenteSuperficies2;
	}


	public int getAgenteSuperficies3() {
		return agenteSuperficies3;
	}


	public void setAgenteSuperficies3(int agenteSuperficies3) {
		this.agenteSuperficies3 = agenteSuperficies3;
	}


	public int getAgenteSuperficies4() {
		return agenteSuperficies4;
	}


	public void setAgenteSuperficies4(int agenteSuperficies4) {
		this.agenteSuperficies4 = agenteSuperficies4;
	}


	public int getEstudioManipuladores() {
		return estudioManipuladores;
	}


	public void setEstudioManipuladores(int estudioManipuladores) {
		this.estudioManipuladores = estudioManipuladores;
	}


	public int getFactorDeterminante() {
		return factorDeterminante;
	}


	public void setFactorDeterminante(int factorDeterminante) {
		this.factorDeterminante = factorDeterminante;
	}


	public String getFechaInvestigacion() {
		return fechaInvestigacion;
	}


	public void setFechaInvestigacion(String fechaInvestigacion) {
		this.fechaInvestigacion = fechaInvestigacion;
	}


	public int getLugarConsumoImplicado() {
		return lugarConsumoImplicado;
	}


	public void setLugarConsumoImplicado(int lugarConsumoImplicado) {
		this.lugarConsumoImplicado = lugarConsumoImplicado;
	}


	public int getMedidaSanitaria() {
		return medidaSanitaria;
	}


	public void setMedidaSanitaria(int medidaSanitaria) {
		this.medidaSanitaria = medidaSanitaria;
	}


	public int getMuestraSuperficies() {
		return muestraSuperficies;
	}


	public void setMuestraSuperficies(int muestraSuperficies) {
		this.muestraSuperficies = muestraSuperficies;
	}


	public String getPaisNotifica() {
		return paisNotifica;
	}


	public void setPaisNotifica(String paisNotifica) {
		this.paisNotifica = paisNotifica;
	}
}
