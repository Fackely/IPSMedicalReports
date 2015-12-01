/**
 * 
 */
package com.servinte.axioma.dto.odontologia.ventaTarjeta;

import java.util.ArrayList;

import net.sf.jasperreports.engine.JRDataSource;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

/**
 * @author armando
 *
 */
public class DtoResultadoReporteVentaTarjetas 
{


	public DtoResultadoReporteVentaTarjetas()
	{
		this.codigoEmpresaInstitucion=ConstantesBD.codigoNuncaValido;
		this.descripcionEmpresaInstitucion="";
		this.centrosAtencion=new ArrayList<DtoResultadoReporteVentaTarjetas.DtoCentroAtencionReporte>();
		this.razonSocial = "";
		this.fechaInicial = "";
		this.fechaFinal = "";
		this.sexoComprador = "";
		this.edad = "";
		this.logoDerecha = "";
		this.logoIzquierda = "";
		this.usuarioProcesa = "";
		this.valorPorInstitucionFormateado = "";
	}
	
	private int codigoEmpresaInstitucion;
	private String descripcionEmpresaInstitucion;
	private ArrayList<DtoCentroAtencionReporte> centrosAtencion;
	
	/**
	 * Objeto jasper que almacena el listado con el resultado arrojado por la consulta.
	 */
	transient private JRDataSource dsListadoResultado;
	
	/**
	 * Objeto jasper que almacena el listado de los centros de atenciòn que arroja la consulta.
	 */
	transient private JRDataSource dsCentrosAtencion;
	
	private String razonSocial;
	private String fechaInicial;
	private String fechaFinal;
	private String edad;
	private String sexoComprador;
	private String logoIzquierda;
	private String logoDerecha;
	private String usuarioProcesa;
	private String valorPorInstitucionFormateado;
	
	
	public int getCodigoEmpresaInstitucion() {
		return codigoEmpresaInstitucion;
	}


	public void setCodigoEmpresaInstitucion(int codigoEmpresaInstitucion) {
		this.codigoEmpresaInstitucion = codigoEmpresaInstitucion;
	}


	public String getDescripcionEmpresaInstitucion() {
		return descripcionEmpresaInstitucion;
	}


	public void setDescripcionEmpresaInstitucion(
			String descripcionEmpresaInstitucion) {
		this.descripcionEmpresaInstitucion = descripcionEmpresaInstitucion;
	}


	public ArrayList<DtoCentroAtencionReporte> getCentrosAtencion() {
		return centrosAtencion;
	}


	public void setCentrosAtencion(
			ArrayList<DtoCentroAtencionReporte> centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}


	public class DtoCentroAtencionReporte
	{
		
		
		public DtoCentroAtencionReporte() 
		{
			super();
			this.consecutivoCentroAtencion = ConstantesBD.codigoNuncaValido;
			this.codigoCentroAtencion="";
			this.descripcionCentroAtencion = "";
			this.codigoCiudadCA = ConstantesBD.codigoNuncaValido;
			this.descripcionCiudadCA = "";
			this.codigoRegionCA = ConstantesBD.codigoNuncaValido;
			this.descripcionRegionCA = "";
			this.codigoPais=ConstantesBD.codigoNuncaValido;
			this.descripcionPais="";
			this.codigoDepartamento=ConstantesBD.codigoNuncaValido;
			this.descripcionDepartamento="";
			this.tiposTarjetas=new ArrayList<DtoResultadoReporteVentaTarjetas.DtoCentroAtencionReporte.DtoTiposTarjetas>();
			this.codigoEmpresaInstitucion=ConstantesBD.codigoNuncaValido;
			this.descripcionEmpresaInstitucion = "";
			this.valorTotalPorCAFormateado = "";
		}

		/**
		 * 
		 */
		private int consecutivoCentroAtencion;
		
		/**
		 * 
		 */
		private String codigoCentroAtencion; 
		
		/**
		 * 
		 */
		private String descripcionCentroAtencion;
		
		/**
		 * 
		 */
		private int codigoCiudadCA;
		
		/**
		 * 
		 */
		private String descripcionCiudadCA;
		
		/**
		 * 
		 */
		private int codigoRegionCA;
		
		/**
		 * 
		 */
		private ArrayList<DtoTiposTarjetas> tiposTarjetas;
		
		
		/**
		 * 
		 */
		private int codigoPais;
		
		/**
		 * 
		 */
		private String descripcionPais;
		
		/**
		 * 
		 */
		private int codigoDepartamento;
		
		/**
		 * 
		 */
		private String descripcionDepartamento;
		
		/**
		 * 
		 */
		private String descripcionRegionCA;
		
		private int codigoEmpresaInstitucion;
		
		private String descripcionEmpresaInstitucion; 
		
		/**
		 * Objeto jasper que almacena el listado de los tipos de tarjetas  
		 * y su informacion correspondiente.
		 */
		transient private JRDataSource dsTiposTarjetas;
		
		private String valorTotalPorCAFormateado;
		

		public int getConsecutivoCentroAtencion() {
			return consecutivoCentroAtencion;
		}

		public void setConsecutivoCentroAtencion(int consecutivoCentroAtencion) {
			this.consecutivoCentroAtencion = consecutivoCentroAtencion;
		}

		public String getDescripcionCentroAtencion() {
			return descripcionCentroAtencion;
		}

		public void setDescripcionCentroAtencion(String descripcionCentroAtencion) {
			this.descripcionCentroAtencion = descripcionCentroAtencion;
		}

		public int getCodigoCiudadCA() {
			return codigoCiudadCA;
		}

		public void setCodigoCiudadCA(int codigoCiudadCA) {
			this.codigoCiudadCA = codigoCiudadCA;
		}

		public String getDescripcionCiudadCA() {
			return descripcionCiudadCA;
		}

		public void setDescripcionCiudadCA(String descripcionCiudadCA) {
			this.descripcionCiudadCA = descripcionCiudadCA;
		}

		public int getCodigoRegionCA() {
			return codigoRegionCA;
		}

		public void setCodigoRegionCA(int codigoRegionCA) {
			this.codigoRegionCA = codigoRegionCA;
		}

		public String getDescripcionRegionCA() {
			return descripcionRegionCA;
		}

		public void setDescripcionRegionCA(String descripcionRegionCA) {
			this.descripcionRegionCA = descripcionRegionCA;
		}
		
		public String getCodigoCentroAtencion() {
			return codigoCentroAtencion;
		}

		public void setCodigoCentroAtencion(String codigoCentroAtencion) {
			this.codigoCentroAtencion = codigoCentroAtencion;
		}

		public int getCodigoPais() {
			return codigoPais;
		}

		public void setCodigoPais(int codigoPais) {
			this.codigoPais = codigoPais;
		}

		public String getDescripcionPais() {
			return descripcionPais;
		}

		public void setDescripcionPais(String descripcionPais) {
			this.descripcionPais = descripcionPais;
		}

		public int getCodigoDepartamento() {
			return codigoDepartamento;
		}

		public void setCodigoDepartamento(int codigoDepartamento) {
			this.codigoDepartamento = codigoDepartamento;
		}

		public String getDescripcionDepartamento() {
			return descripcionDepartamento;
		}

		public void setDescripcionDepartamento(String descripcionDepartamento) {
			this.descripcionDepartamento = descripcionDepartamento;
		}
		
		public ArrayList<DtoTiposTarjetas> getTiposTarjetas() {
			return tiposTarjetas;
		}

		public void setTiposTarjetas(ArrayList<DtoTiposTarjetas> tiposTarjetas) {
			this.tiposTarjetas = tiposTarjetas;
		}

		public class DtoTiposTarjetas
		{
			
			public void DtoTiposTarjetas()
			{
				this.codigoTipoTarjeta=ConstantesBD.codigoNuncaValido;
				this.descripcionTipoTarjeta="";
				this.claseVentaTarjeta=new ArrayList<DtoResultadoReporteVentaTarjetas.DtoCentroAtencionReporte.DtoTiposTarjetas.DtoClaseVentaTarjeta>();
			}
			
			/**
			 * 
			 */
			private int codigoTipoTarjeta;
			
			/**
			 * 
			 */
			private String descripcionTipoTarjeta;
			
			
			private ArrayList<DtoClaseVentaTarjeta> claseVentaTarjeta;
			
			/**
			 * Objeto jasper que contiene el listado con las clases de venta de tarjetas.
			 */
			transient private JRDataSource dsClaseVentaTarjeta;
			
			
			
			
			public int getCodigoTipoTarjeta() {
				return codigoTipoTarjeta;
			}

			public void setCodigoTipoTarjeta(int codigoTipoTarjeta) {
				this.codigoTipoTarjeta = codigoTipoTarjeta;
			}

			public String getDescripcionTipoTarjeta() {
				return descripcionTipoTarjeta;
			}

			public void setDescripcionTipoTarjeta(String descripcionTipoTarjeta) {
				this.descripcionTipoTarjeta = descripcionTipoTarjeta;
			}

			
			public ArrayList<DtoClaseVentaTarjeta> getClaseVentaTarjeta() {
				return claseVentaTarjeta;
			}

			public void setClaseVentaTarjeta(
					ArrayList<DtoClaseVentaTarjeta> claseVentaTarjeta) {
				this.claseVentaTarjeta = claseVentaTarjeta;
			}


			public class DtoClaseVentaTarjeta
			{
				
				public void DtoClaseVentaTarjeta()
				{
					this.claseTarjeta="";
					this.infoVentaTarjeta=new ArrayList<DtoResultadoReporteVentaTarjetas.DtoCentroAtencionReporte.DtoTiposTarjetas.DtoClaseVentaTarjeta.DtoInfoVentaTarjeta>();
					this.descripcionClaseTarjeta = "";
					this.valorTotalClaseTarjeta = ConstantesBD.codigoNuncaValidoDouble;
					this.valorTotalFormateado = "";
				}
				
				private String claseTarjeta;
				
				private ArrayList<DtoInfoVentaTarjeta> infoVentaTarjeta;
				
				private String descripcionClaseTarjeta;
				
				private double valorTotalClaseTarjeta;
				
				private String valorTotalFormateado;
				
				/**
				 * Objeto jasper que contiene el listado con la informacion de la venta
				 * de tarjetas.
				 */
				transient private JRDataSource dsInfoVentaTarjeta;
				
				public String getClaseTarjeta() {
					return claseTarjeta;
				}

				public void setClaseTarjeta(String claseTarjeta) {
					this.claseTarjeta = claseTarjeta;
					
					this.descripcionClaseTarjeta = ValoresPorDefecto.getIntegridadDominio(this.claseTarjeta).toString();
				}

				public ArrayList<DtoInfoVentaTarjeta> getInfoVentaTarjeta() {
					return infoVentaTarjeta;
				}

				public void setInfoVentaTarjeta(ArrayList<DtoInfoVentaTarjeta> infoVentaTarjeta) {
					this.infoVentaTarjeta = infoVentaTarjeta;
				}

				public class DtoInfoVentaTarjeta
				{
					/**
					 * 
					 */
					public void DtoInfoVentaTarjeta()
					{
						this.serialInicial="";
						this.serialFinal="";
						this.cantidad=0;
						this.fechaVenta="";
						this.nroFactura="";
						this.usuarioVendedor="";
						this.convenioTarifa="";
						this.primerNombreComprador="";
						this.segundoNombreComprador="";
						this.primerApellidoComprador="";
						this.segundoApellidoComprador="";
						this.valorTotalVenta=0;
						this.usuarioComprador = "";
						this.valorTotalVentaFormateado = "";
						this.sexoComprador = "";
						this.edadComprador = "";
					}
					
					private String serialInicial;
					
					private String serialFinal;
					
					private int cantidad;
					
					private String fechaVenta;
					
					private String nroFactura;
					
					private String usuarioVendedor;
					
					private String convenioTarifa;
					
					private String primerNombreComprador;
					
					private String segundoNombreComprador;
					
					private String primerApellidoComprador;
					
					private String segundoApellidoComprador;
					
					private double valorTotalVenta;
					
					private String usuarioComprador;
					
					private String valorTotalVentaFormateado;
					
					private String sexoComprador;
					
					private String edadComprador;
					
					
					public String getSerialInicial() {
						
						if (UtilidadTexto.isEmpty(this.serialInicial)) {
							this.serialInicial = "-";
						}
						
						return serialInicial;
					}

					public void setSerialInicial(String serialInicial) {
						this.serialInicial = serialInicial;
					}

					public String getSerialFinal() {
						
						if (UtilidadTexto.isEmpty(this.serialFinal)) {
							this.serialFinal = "-";
						}
						
						return serialFinal;
					}

					public void setSerialFinal(String serialFinal) {
						this.serialFinal = serialFinal;
					}

					public int getCantidad() {
						return cantidad;
					}

					public void setCantidad(int cantidad) {
						this.cantidad = cantidad;
					}

					public String getFechaVenta() {
						return fechaVenta;
					}

					public void setFechaVenta(String fechaVenta) {
						this.fechaVenta = fechaVenta;
					}

					public String getNroFactura() {
						return nroFactura;
					}

					public void setNroFactura(String nroFactura) {
						this.nroFactura = nroFactura;
					}

					public String getUsuarioVendedor() {
						return usuarioVendedor;
					}

					public void setUsuarioVendedor(String usuarioVendedor) {
						this.usuarioVendedor = usuarioVendedor;
					}

					public String getConvenioTarifa() {
						return convenioTarifa;
					}

					public void setConvenioTarifa(String convenioTarifa) {
						this.convenioTarifa = convenioTarifa;
					}

					public String getPrimerNombreComprador() {
						return primerNombreComprador;
					}

					public void setPrimerNombreComprador(String primerNombreComprador) {
						this.primerNombreComprador = primerNombreComprador;
					}

					public String getSegundoNombreComprador() {
						return segundoNombreComprador;
					}

					public void setSegundoNombreComprador(String segundoNombreComprador) {
						this.segundoNombreComprador = segundoNombreComprador;
					}

					public String getPrimerApellidoComprador() {
						return primerApellidoComprador;
					}

					public void setPrimerApellidoComprador(String primerApellidoComprador) {
						this.primerApellidoComprador = primerApellidoComprador;
					}

					public String getSegundoApellidoComprador() {
						return segundoApellidoComprador;
					}

					public void setSegundoApellidoComprador(String segundoApellidoComprador) {
						this.segundoApellidoComprador = segundoApellidoComprador;
					}

					public double getValorTotalVenta() {
						return valorTotalVenta;
					}

					public void setValorTotalVenta(double valorTotalVenta) {
						this.valorTotalVenta = valorTotalVenta;
					}

					public String getUsuarioComprador() {
						
						if (this.primerNombreComprador.trim().equals(null)) {
							this.primerNombreComprador = "";
						}
						if (this.segundoNombreComprador.trim().equals(null)) {
							this.segundoNombreComprador = "";
						}
						if (this.primerApellidoComprador.trim().equals(null)) {
							this.primerApellidoComprador = "";
						}
						if (this.segundoApellidoComprador.trim().equals(null)) {
							this.segundoApellidoComprador = "";
						}
						
						this.usuarioComprador = this.primerNombreComprador + " " + this.segundoNombreComprador + " "+
							this.primerApellidoComprador + " " + this.segundoApellidoComprador;
						
						return usuarioComprador;
					}

					public void setUsuarioComprador(String usuarioComprador) {
						this.usuarioComprador = usuarioComprador;
					}

					/**
					 * Método que se encarga de obtener el 
					 * valor del atributo  valorTotalVentaFormateado
					 *
					 * @return retorna la variable valorTotalVentaFormateado
					 */
					public String getValorTotalVentaFormateado() {
						
						this.valorTotalVentaFormateado = UtilidadTexto.formatearValores(this.valorTotalVenta);
						
						return valorTotalVentaFormateado;
					}

					/**
					 * Método que se encarga de establecer el valor
					 * del atributo valorTotalVentaFormateado
					 * @param valorTotalVentaFormateado es el valor para el atributo valorTotalVentaFormateado 
					 */
					public void setValorTotalVentaFormateado(String valorTotalVentaFormateado) {
						this.valorTotalVentaFormateado = valorTotalVentaFormateado;
					}

					/**
					 * Método que se encarga de obtener el 
					 * valor del atributo  sexoComprador
					 *
					 * @return retorna la variable sexoComprador
					 */
					public String getSexoComprador() {
						return sexoComprador;
					}

					/**
					 * Método que se encarga de establecer el valor
					 * del atributo sexoComprador
					 * @param sexoComprador es el valor para el atributo sexoComprador 
					 */
					public void setSexoComprador(String sexoComprador) {
						this.sexoComprador = sexoComprador;
						
						
						String sexo = this.sexoComprador.toUpperCase();
						String sexoFemenino= ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoFemenino).toString().toUpperCase();
						String sexoMasculino= ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoMasculino).toString().toUpperCase();
						
						if (sexo.trim().equals(sexoFemenino)) {
							this.sexoComprador = ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoFemenino).toString();
						}else if (sexo.trim().equals(sexoMasculino)) {
							this.sexoComprador = ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoMasculino).toString();
						}

					}

					/**
					 * Método que se encarga de obtener el 
					 * valor del atributo  edadComprador
					 *
					 * @return retorna la variable edadComprador
					 */
					public String getEdadComprador() {
						return edadComprador;
					}

					/**
					 * Método que se encarga de establecer el valor
					 * del atributo edadComprador
					 * @param edadComprador es el valor para el atributo edadComprador 
					 */
					public void setEdadComprador(String edadComprador) {
						this.edadComprador = edadComprador;
						
						String [] fecha = this.edadComprador.split("-");
						String fechaNcto = fecha [2] + "/" + fecha [1] + "/" + fecha [0];
						
						
						if (fechaNcto != null) {
							this.edadComprador = String.valueOf(UtilidadFecha.calcularEdad(fechaNcto));
						}
					}
				}

				public String getDescripcionClaseTarjeta() {
					return descripcionClaseTarjeta;
				}

				public void setDescripcionClaseTarjeta(String descripcionClaseTarjeta) {
					this.descripcionClaseTarjeta = descripcionClaseTarjeta;
				}

				public JRDataSource getDsInfoVentaTarjeta() {
					return dsInfoVentaTarjeta;
				}

				public void setDsInfoVentaTarjeta(JRDataSource dsInfoVentaTarjeta) {
					this.dsInfoVentaTarjeta = dsInfoVentaTarjeta;
				}

				/**
				 * Método que se encarga de obtener el 
				 * valor del atributo  valorTotalClaseTarjeta
				 *
				 * @return retorna la variable valorTotalClaseTarjeta
				 */
				public double getValorTotalClaseTarjeta() {
					return valorTotalClaseTarjeta;
				}

				/**
				 * Método que se encarga de establecer el valor
				 * del atributo valorTotalClaseTarjeta
				 * @param valorTotalClaseTarjeta es el valor para el atributo valorTotalClaseTarjeta 
				 */
				public void setValorTotalClaseTarjeta(double valorTotalClaseTarjeta) {
					this.valorTotalClaseTarjeta = valorTotalClaseTarjeta;
				}

				/**
				 * Método que se encarga de obtener el 
				 * valor del atributo  valorTotalFormateado
				 *
				 * @return retorna la variable valorTotalFormateado
				 */
				public String getValorTotalFormateado() {
					
					this.valorTotalFormateado = UtilidadTexto.formatearValores(this.valorTotalClaseTarjeta);
					
					return valorTotalFormateado;
				}

				/**
				 * Método que se encarga de establecer el valor
				 * del atributo valorTotalFormateado
				 * @param valorTotalFormateado es el valor para el atributo valorTotalFormateado 
				 */
				public void setValorTotalFormateado(String valorTotalFormateado) {
					this.valorTotalFormateado = valorTotalFormateado;
				}
			}


			public JRDataSource getDsClaseVentaTarjeta() {
				return dsClaseVentaTarjeta;
			}

			public void setDsClaseVentaTarjeta(JRDataSource dsClaseVentaTarjeta) {
				this.dsClaseVentaTarjeta = dsClaseVentaTarjeta;
			}
			
		}

		public int getCodigoEmpresaInstitucion() {
			return codigoEmpresaInstitucion;
		}

		public void setCodigoEmpresaInstitucion(int codigoEmpresaInstitucion) {
			this.codigoEmpresaInstitucion = codigoEmpresaInstitucion;
		}

		public String getDescripcionEmpresaInstitucion() {
			return descripcionEmpresaInstitucion;
		}

		public void setDescripcionEmpresaInstitucion(
				String descripcionEmpresaInstitucion) {
			this.descripcionEmpresaInstitucion = descripcionEmpresaInstitucion;
		}

		public JRDataSource getDsTiposTarjetas() {
			return dsTiposTarjetas;
		}

		public void setDsTiposTarjetas(JRDataSource dsTiposTarjetas) {
			this.dsTiposTarjetas = dsTiposTarjetas;
		}

		/**
		 * Método que se encarga de obtener el 
		 * valor del atributo  valorTotalPorCAFormateado
		 *
		 * @return retorna la variable valorTotalPorCAFormateado
		 */
		public String getValorTotalPorCAFormateado() {
			return valorTotalPorCAFormateado;
		}

		/**
		 * Método que se encarga de establecer el valor
		 * del atributo valorTotalPorCAFormateado
		 * @param valorTotalPorCAFormateado es el valor para el atributo valorTotalPorCAFormateado 
		 */
		public void setValorTotalPorCAFormateado(String valorTotalPorCAFormateado) {
			this.valorTotalPorCAFormateado = valorTotalPorCAFormateado;
		}
	}


	public JRDataSource getDsListadoResultado() {
		return dsListadoResultado;
	}


	public void setDsListadoResultado(JRDataSource dsListadoResultado) {
		this.dsListadoResultado = dsListadoResultado;
	}


	public String getRazonSocial() {
		return razonSocial;
	}


	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}


	public String getFechaInicial() {
		return fechaInicial;
	}


	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}


	public String getFechaFinal() {
		return fechaFinal;
	}


	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}


	public String getSexoComprador() {
		return sexoComprador;
	}


	public void setSexoComprador(String sexoComprador) {
		this.sexoComprador = sexoComprador;
	}


	public String getEdad() {
		return edad;
	}


	public void setEdad(String edad) {
		this.edad = edad;
	}


	public String getLogoIzquierda() {
		return logoIzquierda;
	}


	public void setLogoIzquierda(String logoIzquierda) {
		this.logoIzquierda = logoIzquierda;
	}


	public String getLogoDerecha() {
		return logoDerecha;
	}


	public void setLogoDerecha(String logoDerecha) {
		this.logoDerecha = logoDerecha;
	}


	public String getUsuarioProcesa() {
		return usuarioProcesa;
	}


	public void setUsuarioProcesa(String usuarioProcesa) {
		this.usuarioProcesa = usuarioProcesa;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  dsCentrosAtencion
	 *
	 * @return retorna la variable dsCentrosAtencion
	 */
	public JRDataSource getDsCentrosAtencion() {
		return dsCentrosAtencion;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo dsCentrosAtencion
	 * @param dsCentrosAtencion es el valor para el atributo dsCentrosAtencion 
	 */
	public void setDsCentrosAtencion(JRDataSource dsCentrosAtencion) {
		this.dsCentrosAtencion = dsCentrosAtencion;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  valorPorInstitucionFormateado
	 *
	 * @return retorna la variable valorPorInstitucionFormateado
	 */
	public String getValorPorInstitucionFormateado() {
		return valorPorInstitucionFormateado;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo valorPorInstitucionFormateado
	 * @param valorPorInstitucionFormateado es el valor para el atributo valorPorInstitucionFormateado 
	 */
	public void setValorPorInstitucionFormateado(
			String valorPorInstitucionFormateado) {
		this.valorPorInstitucionFormateado = valorPorInstitucionFormateado;
	}
}
