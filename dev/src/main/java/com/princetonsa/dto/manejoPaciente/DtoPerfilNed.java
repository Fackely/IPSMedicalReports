package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;

import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;

public class DtoPerfilNed implements Serializable , Cloneable {
	
	
	

		private double codigoPk;
		private double codigoPaciente;
		private double escala;
		private double escalaFactorPrediccion;
		private double valorTotal;
		private int institucion;
		private DtoInfoFechaUsuario datosfechaUsuarioModifica= new DtoInfoFechaUsuario();
		private DtoInfoFechaUsuario datosfechaUsuarioRegistro= new DtoInfoFechaUsuario();
		private ArrayList<DtoCamposPerfilNed> arrayCampos = new ArrayList<DtoCamposPerfilNed>();
		
		public DtoPerfilNed(){
			this.reset();
		}
		
		public void reset(){
			this.codigoPaciente = ConstantesBD.codigoNuncaValido;
			this.codigoPk = ConstantesBD.codigoNuncaValido;
			this.escala = ConstantesBD.codigoNuncaValido;
			this.institucion = ConstantesBD.codigoNuncaValido;
			this.escalaFactorPrediccion = ConstantesBD.codigoNuncaValido;
			this.valorTotal = ConstantesBD.codigoNuncaValido;
			this.datosfechaUsuarioModifica = new DtoInfoFechaUsuario();
			this.datosfechaUsuarioRegistro = new DtoInfoFechaUsuario();
			this.arrayCampos = new ArrayList<DtoCamposPerfilNed>();
		}
		/**
		 * @return the codigoPk
		 */
		public double getCodigoPk() {
			return codigoPk;
		}
		/**
		 * @param codigoPk the codigoPk to set
		 */
		public void setCodigoPk(double codigoPk) {
			this.codigoPk = codigoPk;
		}
		/**
		 * @return the codigoPaciente
		 */
		public double getCodigoPaciente() {
			return codigoPaciente;
		}
		/**
		 * @param codigoPaciente the codigoPaciente to set
		 */
		public void setCodigoPaciente(double codigoPaciente) {
			this.codigoPaciente = codigoPaciente;
		}
		/**
		 * @return the escala
		 */
		public double getEscala() {
			return escala;
		}
		/**
		 * @param escala the escala to set
		 */
		public void setEscala(double escala) {
			this.escala = escala;
		}
		/**
		 * @return the escalaFactorPrediccion
		 */
		public double getEscalaFactorPrediccion() {
			return escalaFactorPrediccion;
		}
		/**
		 * @param escalaFactorPrediccion the escalaFactorPrediccion to set
		 */
		public void setEscalaFactorPrediccion(double escalaFactorPrediccion) {
			this.escalaFactorPrediccion = escalaFactorPrediccion;
		}
		/**
		 * @return the valorTotal
		 */
		public double getValorTotal() {
			return valorTotal;
		}
		/**
		 * @param valorTotal the valorTotal to set
		 */
		public void setValorTotal(double valorTotal) {
			this.valorTotal = valorTotal;
		}
		/**
		 * @return the datosfechaUsuarioModifica
		 */
		public DtoInfoFechaUsuario getDatosfechaUsuarioModifica() {
			return datosfechaUsuarioModifica;
		}
		/**
		 * @param datosfechaUsuarioModifica the datosfechaUsuarioModifica to set
		 */
		public void setDatosfechaUsuarioModifica(
				DtoInfoFechaUsuario datosfechaUsuarioModifica) {
			this.datosfechaUsuarioModifica = datosfechaUsuarioModifica;
		}
		/**
		 * @return the datosfechaUsuarioRegistro
		 */
		public DtoInfoFechaUsuario getDatosfechaUsuarioRegistro() {
			return datosfechaUsuarioRegistro;
		}
		/**
		 * @param datosfechaUsuarioRegistro the datosfechaUsuarioRegistro to set
		 */
		public void setDatosfechaUsuarioRegistro(
				DtoInfoFechaUsuario datosfechaUsuarioRegistro) {
			this.datosfechaUsuarioRegistro = datosfechaUsuarioRegistro;
		}
		

		/***
		 * 
		 *  
		 */
		public Object clone(){
		        Object obj=null;
		        try{
		            obj=super.clone();
		        }catch(CloneNotSupportedException ex){
		        	Log4JManager.error("no se puede duplicar");
		        }
		        return obj;
		    }
		/**
		 * @return the institucion
		 */
		public int getInstitucion() {
			return institucion;
		}
		/**
		 * @param institucion the institucion to set
		 */
		public void setInstitucion(int institucion) {
			this.institucion = institucion;
		}

		/**
		 * @return the arrayCampos
		 */
		public ArrayList<DtoCamposPerfilNed> getArrayCampos() {
			return arrayCampos;
		}

		/**
		 * @param arrayCampos the arrayCampos to set
		 */
		public void setArrayCampos(ArrayList<DtoCamposPerfilNed> arrayCampos) {
			this.arrayCampos = arrayCampos;
		}

		
	
}
