package com.princetonsa.dto.interfaz;

import java.io.Serializable;
import java.util.ArrayList;

import com.princetonsa.mundo.interfaz.GeneracionInterfaz1E;

public class DtoInterfazLineaS1E implements Serializable
{
	private String tipoLinea;	
	
	private ArrayList<DtoInterfazCampoS1E> arrayCampos;	
	private ArrayList<DtoInterfazLineaS1E> arrayDetalle;
	//Usado para almacenar los distintos tipos de numeros de documentos que pude tener un documneto
	private ArrayList<DtoInterfazDatosDocumentoS1E> arrayDocumentos;
	
	private boolean existeInconsistencia;
	
	/**
	 * Atributos para almacenar la suma debito y credito del documento
	 */
	private double sumaDebito;
	private double sumaCredito;
	private boolean repetido;
	
	
	public DtoInterfazLineaS1E(String tipo)
	{
		reset(tipo);
	}
	
	public void reset(String tipo)
	{
		tipoLinea = tipo;
		arrayCampos = new ArrayList<DtoInterfazCampoS1E>();
		arrayDetalle = new ArrayList<DtoInterfazLineaS1E>();
		arrayDocumentos = new ArrayList<DtoInterfazDatosDocumentoS1E>();
		existeInconsistencia = false;
		
		if(tipo.equals(GeneracionInterfaz1E.indicadorLineaInicio))
			inicializarLineaInicio(GeneracionInterfaz1E.indicadorLineaInicio);
		else if(tipo.equals(GeneracionInterfaz1E.indicadorLineaFinal))
			inicializarLineaFin(GeneracionInterfaz1E.indicadorLineaFinal);
		else if(tipo.equals(GeneracionInterfaz1E.indicadorLineaDocumentoContable))
			inicializarLineaDocumentoContable(GeneracionInterfaz1E.indicadorLineaDocumentoContable);
		else if(tipo.equals(GeneracionInterfaz1E.indicadorLineaDetMovDocumentoContable))
			inicializarLineaDetalleDocumentoContable(GeneracionInterfaz1E.indicadorLineaDetMovDocumentoContable);
		else if(tipo.equals(GeneracionInterfaz1E.indicadorLineaDetCxC))
			inicializarLineaDetalleDocumentoCxC(GeneracionInterfaz1E.indicadorLineaDetCxC);
		else if(tipo.equals(GeneracionInterfaz1E.indicadorLineaDetCxP))
			inicializarLineaDetalleDocumentoCxP(GeneracionInterfaz1E.indicadorLineaDetCxP);
		else if(tipo.equals(GeneracionInterfaz1E.indicadorLineaEventoCartera))
		{
			inicializarLineaEventoCartera(GeneracionInterfaz1E.indicadorLineaEventoCartera);
		}
		
		this.sumaDebito = 0;
		this.sumaCredito = 0;
		this.repetido = false;
	}
	
	private void inicializarLineaEventoCartera(String indicadorLinea) 
	{
		//1
		DtoInterfazCampoS1E campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Numero de Registro");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(7);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//2
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Tipo de Registro");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(4);
		campo.setValor("0094");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//3
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Subtipo de Registro");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(2);
		campo.setValor("01");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//4
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Version del Tipo de Registro");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(2);
		campo.setValor("01");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//5
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Compania");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(3);	
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//6
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Tipo de evento para el seguimiento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(5);	
		campo.setValor("00301");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//7
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Código del seguimiento del evento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(true);
		campo.setTamano(20);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//8
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Fecha de seguimiento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoFecha);
		campo.setRequerido(true);
		campo.setTamano(8);	
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//9
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Hora de seguimiento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoHora);
		campo.setRequerido(true);
		campo.setTamano(8);	
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//10
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Notas del seguimiento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(true);
		campo.setTamano(2000);	
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//11
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Referencia de Seguimiento del Evento 1");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(true);
		campo.setTamano(50);	
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//12
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Referencia de Seguimiento del Evento 2");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(true);
		campo.setTamano(50);	
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);		
		
		//13
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Valor 1 del seguimiento del evento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumValorMov);
		campo.setRequerido(false);
		campo.setTamano(20);	
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//14
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Valor 2 del seguimiento del evento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumValorMov);
		campo.setRequerido(false);
		campo.setTamano(20);	
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//15
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Fecha");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoFecha);
		campo.setRequerido(false);
		campo.setTamano(8);	
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//16
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Auxiliar de cuenta contable");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(true);
		campo.setTamano(20);	
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//17
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Tercero");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(false);
		campo.setTamano(15);	
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//18
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Centro de operacion del movimiento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(false);
		campo.setTamano(3);	
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//19
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Unidad de negocio");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(false);
		campo.setTamano(20);	
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//20
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Sucursal del cliente");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(true);
		campo.setTamano(3);
		campo.setValor("001");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//21
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Tipo de documento cruce");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(true);
		campo.setTamano(3);	
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//22
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Numero de documento cruce");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(8);	
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//23
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Numero de cuota de documento cruce");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(3);
		campo.setValor("000");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		
		
		                                  
	}

	public void inicializarLineaInicio(String indicadorLinea)
	{
		//1
		DtoInterfazCampoS1E campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Numero de Registro");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(7);
		campo.setValor("1");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//2
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Tipo de Registro");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(4);
		campo.setValor("0000");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//3
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Subtipo de Registro");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(2);
		campo.setValor("00");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Version del Tipo de Registro");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(2);
		campo.setValor("01");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Compania");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(3);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
	}
	
	public void inicializarLineaDocumentoContable(String indicadorLinea)
	{
		//1
		DtoInterfazCampoS1E campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Numero de Registro");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(7);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//2
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Tipo de Registro");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(4);
		campo.setValor("350");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//3
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Subtipo de Registro");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(2);
		campo.setValor("00");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//4
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Version del Tipo de Registro");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(2);
		campo.setValor("01");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//5
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Compania");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(3);	
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//6
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Indicativo Numero Consecutivo de Documento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(1);
		campo.setValor("0");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//7
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Centro de Operacion del Documento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(true);
		campo.setTamano(3);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//8
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Tipo de Documento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(true);
		campo.setTamano(3);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//9
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Numero de Documento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(8);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//10
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Fecha del Documento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoFecha);
		campo.setRequerido(true);
		campo.setTamano(8);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//11
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Tercero del Documento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(false);
		campo.setTamano(15);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//12
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Clase Interna del Documento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(5);
		campo.setValor("30");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//13
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Estado del Documento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(1);
		campo.setValor("1");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//14
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Estado de Impresion del Documento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(1);
		campo.setValor("1");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//15
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Observaciones del Documento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(false);
		campo.setTamano(255);		
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
	}
	
	public void inicializarLineaDetalleDocumentoContable(String indicadorLinea)
	{
		//1
		DtoInterfazCampoS1E campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Numero de Registro");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(7);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//2
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Tipo de Registro");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(4);
		campo.setValor("351");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//3
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Subtipo de Registro");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(2);
		campo.setValor("00");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//4
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Version del Tipo de Registro");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(2);
		campo.setValor("01");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//5
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Compania");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(3);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//6
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Centro de Operacion del Documento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(true);
		campo.setTamano(3);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//7
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Tipo de Documento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(true);
		campo.setTamano(3);		
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//8
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Numero de Documento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(8);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//9
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Auxiliar de Cuenta Contable");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(true);
		campo.setTamano(20);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//10
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Tercero");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(false);
		campo.setTamano(15);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//11
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Centro de Operacion del Movimiento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(false);
		campo.setTamano(3);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//12
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Unidad de Negocio");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(false);
		campo.setTamano(2);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//13
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Auxiliar de Centro de Costos");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(false);
		campo.setTamano(15);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//14
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Auxiliar de Concepto de Flujo de Efectivo");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(false);
		campo.setTamano(10);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//15
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Valor Debito");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumValorMov);
		campo.setRequerido(false);
		campo.setTamano(21);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//16
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Valor Credito");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumValorMov);
		campo.setRequerido(false);
		campo.setTamano(21);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//17
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Valor Debito Alterno");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumValorMov);
		campo.setRequerido(false);
		campo.setTamano(21);
		campo.setValor("0");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//18
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Valor Credito Alterno");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumValorMov);
		campo.setRequerido(false);
		campo.setTamano(21);
		campo.setValor("0");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);

		//19
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Valor Base Gravable");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumValorMov);
		campo.setRequerido(false);
		campo.setTamano(21);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//20
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Tipo de Documento de Banco");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(false);
		campo.setTamano(2);
		campo.setValor("  ");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//21
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Numero de Documento de Banco");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(false);
		campo.setTamano(8);
		campo.setValor("0");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//22
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Observaciones del Movimiento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(false);
		campo.setTamano(255);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
	}
	
	public void inicializarLineaDetalleDocumentoCxC(String indicadorLinea)
	{
		//0
		DtoInterfazCampoS1E campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Numero de Registro");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(7);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//1
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Tipo de Registro");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(4);
		campo.setValor("351");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//2
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Subtipo de Registro");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(2);
		campo.setValor("01");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//3
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Version del Tipo de Registro");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(2);
		campo.setValor("01");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//4
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Compania");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(3);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//5
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Centro de Operacion del Documento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(true);
		campo.setTamano(3);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//6
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Tipo de Documento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(true);
		campo.setTamano(3);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//7
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Numero de Documento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(8);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//8
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Auxiliar de Cuenta Contable");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(true);
		campo.setTamano(20);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//9
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Tercero");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(false);
		campo.setTamano(15);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//10
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Centro de Operacion del Movimiento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(false);
		campo.setTamano(3);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//11
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Unidad de Negocio");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(false);
		campo.setTamano(2);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//12
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Auxiliar de Centro de Costos");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(false);
		campo.setTamano(15);
		campo.setValor("               ");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//13
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Valor Debito");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumValorMov);
		campo.setRequerido(false);
		campo.setTamano(21);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//14
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Valor Credito");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumValorMov);
		campo.setRequerido(false);
		campo.setTamano(21);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//15
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Valor Debito Alterno");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumValorMov);
		campo.setRequerido(false);
		campo.setTamano(21);
		campo.setValor("0");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//16
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Valor Credito Alterno");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumValorMov);
		campo.setRequerido(false);
		campo.setTamano(21);
		campo.setValor("0");
		arrayCampos.add(campo);
		
		//17
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Observaciones del Movimiento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(false);
		campo.setTamano(255);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//18
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Sucursal Cliente");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(true);
		campo.setTamano(3);
		campo.setValor("001");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//19
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Tipo de Documento de Cruce");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(true);
		campo.setTamano(3);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//20
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Numero de Documento de Cruce");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(8);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//21
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Numero de Cuota de Documento de Cruce");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(3);
		campo.setValor("000");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//22
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Fecha de Vencimiento del Documento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoFecha);
		campo.setRequerido(true);
		campo.setTamano(8);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//23
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Fecha de Pronto Pago del Documento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoFecha);
		campo.setRequerido(true);
		campo.setTamano(8);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//24
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Valor descuento Pronto Pago Otorgado");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumValorMov);
		campo.setRequerido(false);
		campo.setTamano(21);
		campo.setValor("0");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//25
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Valor descuento Pronto Pago Aplicado");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumValorMov);
		campo.setRequerido(false);
		campo.setTamano(21);
		campo.setValor("0");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//26
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Valor descuento Pronto Pago Aplicado Alterno");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumValorMov);
		campo.setRequerido(false);
		campo.setTamano(21);
		campo.setValor("0");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//27
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Valor Ajuste al Saldo");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumValorMov);
		campo.setRequerido(false);
		campo.setTamano(21);
		campo.setValor("0");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//28
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Valor Ajuste al Saldo Alterno");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumValorMov);
		campo.setRequerido(false);
		campo.setTamano(21);
		campo.setValor("0");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//29
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Valor Retencion");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumValorMov);
		campo.setRequerido(false);
		campo.setTamano(21);
		campo.setValor("0");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//30
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Valor Retencion Alterno");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumValorMov);
		campo.setRequerido(false);
		campo.setTamano(21);
		campo.setValor("0");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//31
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Tercero Vendedor");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(true);
		campo.setTamano(15);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//32
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Observaciones del Movimiento de Saldo Abierto");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(true);
		campo.setTamano(255);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
	}
	
	public void inicializarLineaDetalleDocumentoCxP(String indicadorLinea)
	{
		//0
		DtoInterfazCampoS1E campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Numero de Registro");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(7);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//1
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Tipo de Registro");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(4);
		campo.setValor("351");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//2
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Subtipo de Registro");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(2);
		campo.setValor("02");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//3
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Version del Tipo de Registro");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(2);
		campo.setValor("01");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//4
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Compania");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(3);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//5
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Centro de Operacion del Documento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(true);
		campo.setTamano(3);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//6
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Tipo de Documento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(true);
		campo.setTamano(3);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//7
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Numero de Documento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(8);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//8
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Auxiliar de Cuenta Contable");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(true);
		campo.setTamano(20);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//9
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Tercero");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(false);
		campo.setTamano(15);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//10
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Centro de Operacion del Movimiento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(false);
		campo.setTamano(3);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//11
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Unidad de Negocio");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(false);
		campo.setTamano(2);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//12
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Auxiliar de Centro de Costos");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(false);
		campo.setTamano(15);
		campo.setValor("               ");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//13
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Valor Debito");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumValorMov);
		campo.setRequerido(false);
		campo.setTamano(21);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//14
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Valor Credito");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumValorMov);
		campo.setRequerido(false);
		campo.setTamano(21);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//15
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Valor Debito Alterno");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumValorMov);
		campo.setRequerido(false);
		campo.setTamano(21);
		campo.setValor("0");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//16
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Valor Credito Alterno");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumValorMov);
		campo.setRequerido(false);
		campo.setTamano(21);
		campo.setValor("0");
		arrayCampos.add(campo);
		
		//17
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Observaciones del Movimiento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(false);
		campo.setTamano(255);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//18
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Sucursal Proveedor");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(true);
		campo.setTamano(3);
		campo.setValor("001");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//19
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Prefijo de Documento de Cruce");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(true);
		campo.setTamano(4);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//20
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Numero de Documento de Cruce");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(8);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//21
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Numero de Cuota de Documento de Cruce");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(3);
		campo.setValor("01");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//22
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Auxiliar de Concepto de Flujo de Efectivo");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(true);
		campo.setTamano(10);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//23
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Fecha de Vencimiento del Documento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoFecha);
		campo.setRequerido(true);
		campo.setTamano(8);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//24
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Fecha de Pronto Pago del Documento");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoFecha);
		campo.setRequerido(true);
		campo.setTamano(8);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//25
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Valor descuento Pronto Pago Otorgado");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumValorMov);
		campo.setRequerido(false);
		campo.setTamano(21);
		campo.setValor("0");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//26
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Valor descuento Pronto Pago Aplicado");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumValorMov);
		campo.setRequerido(false);
		campo.setTamano(21);
		campo.setValor("0");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//27
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Valor descuento Pronto Pago Aplicado Alterno");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumValorMov);
		campo.setRequerido(false);
		campo.setTamano(21);
		campo.setValor("0");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//28
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Valor Retencion");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumValorMov);
		campo.setRequerido(false);
		campo.setTamano(21);
		campo.setValor("0");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//29
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Valor Retencion Alterno");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumValorMov);
		campo.setRequerido(false);
		campo.setTamano(21);
		campo.setValor("0");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//30
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Observaciones de Movimiento de Saldo Abierto");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoAlfanumerico);
		campo.setRequerido(true);
		campo.setTamano(255);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
	}
	
	
	public void inicializarLineaFin(String indicadorLinea)
	{
		//1
		DtoInterfazCampoS1E campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Numero de Registro");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(7);
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//2
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Tipo de Registro");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(4);
		campo.setValor("9999");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//3
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Subtipo de Registro");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(2);
		campo.setValor("00");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//4
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Version del Tipo de Registro");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(2);
		campo.setValor("01");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
		
		//5
		campo = new DtoInterfazCampoS1E();
		campo.setDescripcion("Compania");
		campo.setTipo(GeneracionInterfaz1E.codigoTipoNumerico);
		campo.setRequerido(true);
		campo.setTamano(3);
		campo.setValor("");
		campo.setIndicadorLinea(indicadorLinea);
		arrayCampos.add(campo);
	}
	
	public ArrayList<DtoInterfazCampoS1E> getArrayCampos() {
		return arrayCampos;
	}

	public void setArrayCampos(ArrayList<DtoInterfazCampoS1E> arrayCampos) {
		this.arrayCampos = arrayCampos;
	}

	public String getTipoLinea() {
		return tipoLinea;
	}

	public void setTipoLinea(String tipoLinea) {
		this.tipoLinea = tipoLinea;
	}

	public ArrayList<DtoInterfazLineaS1E> getArrayDetalle() {
		return arrayDetalle;
	}

	public void setArrayDetalle(ArrayList<DtoInterfazLineaS1E> arrayDetalle) {
		this.arrayDetalle = arrayDetalle;
	}

	public boolean isExisteInconsistencia() {
		return existeInconsistencia;
	}

	public void setExisteInconsistencia(boolean existeInconsistencia) {
		this.existeInconsistencia = existeInconsistencia;
	}

	/**
	 * @return the arrayDocumentos
	 */
	public ArrayList<DtoInterfazDatosDocumentoS1E> getArrayDocumentos() {
		return arrayDocumentos;
	}

	/**
	 * @param arrayDocumentos the arrayDocumentos to set
	 */
	public void setArrayDocumentos(
			ArrayList<DtoInterfazDatosDocumentoS1E> arrayDocumentos) {
		this.arrayDocumentos = arrayDocumentos;
	}

	/**
	 * @return the sumaDebito
	 */
	public double getSumaDebito() {
		return sumaDebito;
	}

	/**
	 * @param sumaDebito the sumaDebito to set
	 */
	public void setSumaDebito(double sumaDebito) {
		this.sumaDebito = sumaDebito;
	}

	/**
	 * @return the sumaCredito
	 */
	public double getSumaCredito() {
		return sumaCredito;
	}

	/**
	 * @param sumaCredito the sumaCredito to set
	 */
	public void setSumaCredito(double sumaCredito) {
		this.sumaCredito = sumaCredito;
	}

	/**
	 * @return the repetido
	 */
	public boolean isRepetido() {
		return repetido;
	}

	/**
	 * @param repetido the repetido to set
	 */
	public void setRepetido(boolean repetido) {
		this.repetido = repetido;
	}

	
	
}