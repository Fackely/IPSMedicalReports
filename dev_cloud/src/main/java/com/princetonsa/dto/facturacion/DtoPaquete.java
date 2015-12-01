/*
 * Jun 15, 2007
 * Proyect axioma
 * Paquete com.princetonsa.dto.facturacion
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
 */
package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import util.InfoDatosString;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class DtoPaquete implements Serializable{

		/**
		 * Codigo de paqutizacion.
		 */
		private String codigo;
		
		/**
		 * Paquete 
		 */
		private InfoDatosString paquete;
		
		/**
		 * Responsable a la que esta asignado el paquete.
		 */
		private String subCuenta;
		
		/**
		 * Numero de solicitud relacionada al paquete.
		 */
		private String numeroSolicitud;
		
		/**
		 * Detalles del paquete.
		 */
		private Collection<DtoDetallePaquete> detallePaquete;

		/**
		 * 
		 */
		public DtoPaquete() 
		{
			this.codigo="";
			this.paquete=new InfoDatosString();
			this.subCuenta="";
			this.numeroSolicitud="";
			this.detallePaquete=new ArrayList<DtoDetallePaquete>();
		}

		/**
		 * @return the codigo
		 */
		public String getCodigo() {
			return codigo;
		}

		/**
		 * @param codigo the codigo to set
		 */
		public void setCodigo(String codigo) {
			this.codigo = codigo;
		}

		/**
		 * @return the detallePaquete
		 */
		public Collection<DtoDetallePaquete> getDetallePaquete() {
			return detallePaquete;
		}

		/**
		 * @param detallePaquete the detallePaquete to set
		 */
		public void setDetallePaquete(Collection<DtoDetallePaquete> detallePaquete) {
			this.detallePaquete = detallePaquete;
		}

		/**
		 * @return the numeroSolicitud
		 */
		public String getNumeroSolicitud() {
			return numeroSolicitud;
		}

		/**
		 * @param numeroSolicitud the numeroSolicitud to set
		 */
		public void setNumeroSolicitud(String numeroSolicitud) {
			this.numeroSolicitud = numeroSolicitud;
		}

		/**
		 * @return the paquete
		 */
		public InfoDatosString getPaquete() {
			return paquete;
		}

		/**
		 * @param paquete the paquete to set
		 */
		public void setPaquete(InfoDatosString paquete) {
			this.paquete = paquete;
		}

		/**
		 * @return the subCuenta
		 */
		public String getSubCuenta() {
			return subCuenta;
		}

		/**
		 * @param subCuenta the subCuenta to set
		 */
		public void setSubCuenta(String subCuenta) {
			this.subCuenta = subCuenta;
		}
		
		
	

}
