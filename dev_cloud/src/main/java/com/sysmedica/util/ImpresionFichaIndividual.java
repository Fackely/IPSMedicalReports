package com.sysmedica.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import util.UtilidadFecha;

import com.sysmedica.actionform.FichaGenericaForm;

public class ImpresionFichaIndividual {

//	*************************************
    // DATOS CARA A :
        
    // Informacion general
    private String sire;
    private String municipioNotifica;
    private String departamentoNotifica;
    private String lugarNotifica;
    private String codigoInstitucionAtendio;
    private String fechaNotificacion;
    private String nombreEvento;
    private String codigoDiagnostico;
    private String nombreInstitucionAtendio;
    private String semanaEpiNotificacion;
    
    private String unidadesEdad;
    
    // Identificacion del paciente
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String departamentoNacimiento;
    private String ciudadNacimiento;
    private String departamentoResidencia;
    private String ciudadResidencia;
    private String direccionPaciente;
    private String telefonoPaciente;
    private String fechaNacimiento;
    private String edad;
    private String genero;
    private String estadoCivil;
    private String documento;
    private String barrioResidencia;
    private String zonaDomicilio;
    private String ocupacion;
    private String aseguradora;
    private String regimenSalud;
    private String etnia;
    private int desplazado;
    private String tipoId;
    
    private String codCiudadResidencia;
    private String codDepResidencia;
        
    
    // Notificacion
    private String fechaConsultaGeneral;
    private String codigoMunProcedencia;
    private String codigoDepProcedencia;
    private String lugarProcedencia;
    private String fechaInicioSint;
    private int tipoCaso;
    private boolean hospitalizado;
    private String fechaHospitalizacion;
    private boolean estaVivo;
    private String fechaDefuncion;
    private String nombreProfesional;
    
    private String nombreMunProcedencia;
    private String nombreDepProcedencia;
    
    private String rutaEpidemiologia;
    
    private FichaGenericaForm fichaGenericaForm;
    
    public ImpresionFichaIndividual(FichaGenericaForm ficha) {
    	this.fichaGenericaForm = ficha;
    }
    
    public void imprimirFicha()
    {
    	llenarCampos();
    	
        try {
             //   String rutaArchivo = "C:/Documents and Settings/propietario/Escritorio/fichaindividual.jpg";
             //   String rutaArchivoModificado = "C:/Documents and Settings/propietario/Escritorio/fichaindividual2.jpg";
    		rutaEpidemiologia = UtilidadGenArchivos.getRutaEpidemiologia();
    		
    		String rutaArchivo = rutaEpidemiologia+"fichasjpg/fichaindividual.jpg";
    		String rutaArchivoModificado = rutaEpidemiologia+"fichasjpg/tmp/fichaindividual.jpg";
            BufferedImage image = ImageIO.read(new File(rutaArchivo));
            Graphics2D g = image.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);



            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.BOLD, 18));

            g.drawString(""+nombreEvento, 35, 170);
            g.drawString(""+codigoDiagnostico, 480, 170);
            
            try {
	            g.drawString(""+Character.toString(fechaNotificacion.charAt(0)),593,170);
	            g.drawString(""+Character.toString(fechaNotificacion.charAt(1)),614,170);
	            g.drawString(""+Character.toString(fechaNotificacion.charAt(3)),634,170);
	            g.drawString(""+Character.toString(fechaNotificacion.charAt(4)),655,170);
	            g.drawString(""+Character.toString(fechaNotificacion.charAt(6)),675,170);
	            g.drawString(""+Character.toString(fechaNotificacion.charAt(7)),695,170);
	            g.drawString(""+Character.toString(fechaNotificacion.charAt(8)),715,170);
	            g.drawString(""+Character.toString(fechaNotificacion.charAt(9)),737,170);
            }
            catch (NullPointerException npe) {}

            g.drawString(""+semanaEpiNotificacion,28,220);

        //    g.drawString(semanaEpidemiologica,34,270);
            String anyoNoti = fechaNotificacion.split("/")[2];

            g.drawString(""+Character.toString(anyoNoti.charAt(0)), 102, 220);
            g.drawString(""+Character.toString(anyoNoti.charAt(1)), 122, 220);
            g.drawString(""+Character.toString(anyoNoti.charAt(2)), 142, 220);
            g.drawString(""+Character.toString(anyoNoti.charAt(3)), 162, 220);

            g.drawString(""+departamentoNotifica, 200,220);
            g.drawString(""+municipioNotifica, 470, 220);
            g.drawString(""+nombreInstitucionAtendio, 35, 268);

            try {
	            g.drawString(""+Character.toString(codigoInstitucionAtendio.charAt(0)), 512,268);
	            g.drawString(""+Character.toString(codigoInstitucionAtendio.charAt(1)), 532,268);
	            g.drawString(""+Character.toString(codigoInstitucionAtendio.charAt(2)), 552,268);
	            g.drawString(""+Character.toString(codigoInstitucionAtendio.charAt(3)), 573,268);
	            g.drawString(""+Character.toString(codigoInstitucionAtendio.charAt(4)), 594,268);
	            g.drawString(""+Character.toString(codigoInstitucionAtendio.charAt(5)), 614,268);
	            g.drawString(""+Character.toString(codigoInstitucionAtendio.charAt(6)), 634,268);
	            g.drawString(""+Character.toString(codigoInstitucionAtendio.charAt(7)), 655,268);
	            g.drawString(""+Character.toString(codigoInstitucionAtendio.charAt(8)), 675,268);
	            g.drawString(""+Character.toString(codigoInstitucionAtendio.charAt(9)), 696,268);
	            g.drawString(""+Character.toString(codigoInstitucionAtendio.charAt(10)), 716,268);
	            g.drawString(""+Character.toString(codigoInstitucionAtendio.charAt(11)), 737,268);
            }
            catch (StringIndexOutOfBoundsException e) {}


            g.drawString(""+primerNombre, 35, 350);
            g.drawString(""+segundoNombre, 385, 350);
            g.drawString(""+primerApellido, 35,398);
            g.drawString(""+segundoApellido, 385,398);

            if (tipoId.equals("NU")) {
                llenarCuadro(g, 37, 426);
            }
            else if (tipoId.equals("RC")) {
                llenarCuadro(g, 78, 426);
            }
            else if (tipoId.equals("TI")) {
                llenarCuadro(g, 138, 426);
            }
            else if (tipoId.equals("CC")) {
                llenarCuadro(g, 200, 426);
            }
            else if (tipoId.equals("CE")) {
                llenarCuadro(g, 241, 426);
            }
            else if (tipoId.equals("PA")) {
                llenarCuadro(g, 323, 426);
            }
            else if (tipoId.equals("MS")) {
                llenarCuadro(g, 405, 426);
            }
            else if (tipoId.equals("AS")) {
                llenarCuadro(g, 487, 426);
            }

            g.drawString(""+documento,590,447);            
            g.drawString(""+edad, 48, 495);

            if (unidadesEdad.equals("A")) {
                llenarCuadro(g, 98, 474);
            }
            else if (unidadesEdad.equals("M")) {
                llenarCuadro(g, 159, 474);
            }
            else if (unidadesEdad.equals("D")) {
                llenarCuadro(g, 220, 474);
            }
            else if (unidadesEdad.equals("H")) {
                llenarCuadro(g, 261, 474);
            }
            else if (unidadesEdad.equals("M")) {
                llenarCuadro(g, 323, 474);
            }

            if (genero.equals("M")) {
                llenarCuadro(g, 405, 474);
            }
            else if (genero.equals("F")) {
                llenarCuadro(g, 446, 474);
            }

            g.drawString(""+direccionPaciente,510,495);
            g.drawString(""+barrioResidencia,35,545);
            g.drawString(""+ciudadResidencia,265,545);
            g.drawString(""+codDepResidencia,432,545);
            g.drawString(""+codCiudadResidencia,473,545);

            if (zonaDomicilio.equals("U")) {
                llenarCuadro(g, 549, 523);
            }
            else if (zonaDomicilio.equals("R")) {
                llenarCuadro(g, 610, 523);
            }

            g.drawString(""+telefonoPaciente,35,593);
            
            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.BOLD, 12));
            
            g.drawString(""+ocupacion,188,593);
            
            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.BOLD, 18));

            if (regimenSalud.equals("Contributivo")) {
                llenarCuadro(g, 446, 572);
            }
            else if (regimenSalud.equals("Subsidiado")) {
                llenarCuadro(g, 528, 572);
            }
            else if (regimenSalud.equals("Vinculado")) {
                llenarCuadro(g, 590, 572);
            }
            else if (regimenSalud.equals("Particular")) {
                llenarCuadro(g, 651, 572);
            }
            else if (regimenSalud.equals("Otro")) {
                llenarCuadro(g, 713, 572);
            }

            g.drawString(""+aseguradora,35,642);

            if (etnia.equals("I")) {
                llenarCuadro(g, 405,620);
            }
            else if (etnia.equals("A")) {
                llenarCuadro(g, 466,620);
            }
            else if (etnia.equals("O")) {
                llenarCuadro(g, 569,620);
            }

            if (desplazado==1) {
                llenarCuadro(g, 651, 620);
            }
            else if (desplazado==0) {
                llenarCuadro(g, 713, 620);
            }

            g.drawString(""+nombreMunProcedencia,35,725);
            g.drawString(""+codigoDepProcedencia,329,725);
            g.drawString(""+codigoMunProcedencia,369,725);

            try {
	            g.drawString(""+Character.toString(fechaConsultaGeneral.charAt(0)),430,725);
	            g.drawString(""+Character.toString(fechaConsultaGeneral.charAt(1)),450,725);
	            g.drawString(""+Character.toString(fechaConsultaGeneral.charAt(3)),470,725);
	            g.drawString(""+Character.toString(fechaConsultaGeneral.charAt(4)),491,725);
	            g.drawString(""+Character.toString(fechaConsultaGeneral.charAt(6)),512,725);
	            g.drawString(""+Character.toString(fechaConsultaGeneral.charAt(7)),532,725);
	            g.drawString(""+Character.toString(fechaConsultaGeneral.charAt(8)),553,725);
	            g.drawString(""+Character.toString(fechaConsultaGeneral.charAt(9)),574,725);
	            
	            g.drawString(""+Character.toString(fechaInicioSint.charAt(0)),594,725);
	            g.drawString(""+Character.toString(fechaInicioSint.charAt(1)),614,725);
	            g.drawString(""+Character.toString(fechaInicioSint.charAt(3)),634,725);
	            g.drawString(""+Character.toString(fechaInicioSint.charAt(4)),655,725);
	            g.drawString(""+Character.toString(fechaInicioSint.charAt(6)),676,725);
	            g.drawString(""+Character.toString(fechaInicioSint.charAt(7)),696,725);
	            g.drawString(""+Character.toString(fechaInicioSint.charAt(8)),716,725);
	            g.drawString(""+Character.toString(fechaInicioSint.charAt(9)),737,725);
            }
            catch (NullPointerException npe) {}

            
            

            if (tipoCaso==1) {
                llenarCuadro(g, 37, 753);
            }
            else if (tipoCaso==2) {
                llenarCuadro(g, 118, 753);
            }
            else if (tipoCaso==3) {
                llenarCuadro(g, 179, 753);
            }
            else if (tipoCaso==4) {
                llenarCuadro(g, 282, 753);
            }
            else if (tipoCaso==5) {
                llenarCuadro(g, 364, 753);
            }

            if (hospitalizado) {
                llenarCuadro(g, 507, 753);
            }
            else if (!hospitalizado) {
                llenarCuadro(g, 548, 753);
            }


            try {
	            g.drawString(""+Character.toString(fechaHospitalizacion.charAt(0)),594,772);
	            g.drawString(""+Character.toString(fechaHospitalizacion.charAt(1)),614,772);
	            g.drawString(""+Character.toString(fechaHospitalizacion.charAt(3)),634,772);
	            g.drawString(""+Character.toString(fechaHospitalizacion.charAt(4)),655,772);
	            g.drawString(""+Character.toString(fechaHospitalizacion.charAt(6)),676,772);
	            g.drawString(""+Character.toString(fechaHospitalizacion.charAt(7)),696,772);
	            g.drawString(""+Character.toString(fechaHospitalizacion.charAt(8)),716,772);
	            g.drawString(""+Character.toString(fechaHospitalizacion.charAt(9)),737,772);
            }
            catch (NullPointerException npe) {}

            if (estaVivo) {
                llenarCuadro(g, 36, 802);
            }
            else if (!estaVivo) {
                llenarCuadro(g, 98, 802);
            }

            try {
	            g.drawString(""+Character.toString(fechaDefuncion.charAt(0)),163,820);
	            g.drawString(""+Character.toString(fechaDefuncion.charAt(1)),184,820);
	            g.drawString(""+Character.toString(fechaDefuncion.charAt(3)),204,820);
	            g.drawString(""+Character.toString(fechaDefuncion.charAt(4)),224,820);
	            g.drawString(""+Character.toString(fechaDefuncion.charAt(6)),245,820);
	            g.drawString(""+Character.toString(fechaDefuncion.charAt(7)),265,820);
	            g.drawString(""+Character.toString(fechaDefuncion.charAt(8)),285,820);
	            g.drawString(""+Character.toString(fechaDefuncion.charAt(9)),306,820);
            }
            catch (NullPointerException npe) {}

            g.drawString(""+nombreProfesional,340,820);
           
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(rutaArchivoModificado));
            /*JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(image);
            double calidad = 0.9;
            param.setQuality((float)calidad,false);
            encoder.setJPEGEncodeParam(param);
            encoder.encode(image);*/
            out.close();
            
        //    ImageIO.write(image,"JPG",new File(rutaArchivoModificado));
        }
        catch (IOException ioe) {}
    }
    
    
    
    
    public void llenarCampos() {
    	/*
    	FichaGenericaForm fichaGenericaForm = new FichaGenericaForm();
    	
    	if (ficha instanceof FichaTuberculosisForm) {
    		
    		FichaTuberculosisForm fichaTuberculosisForm = (FichaTuberculosisForm)ficha;
    		fichaGenericaForm = fichaTuberculosisForm.getFichaGenericaForm();
    	}
    	else if (ficha instanceof FichaGenericaForm ) {
    		
    		fichaGenericaForm = (FichaGenericaForm)ficha;
    	}
    	*/
    	
    	this.setMunicipioNotifica(fichaGenericaForm.getNombreMunNotifica());
        this.setDepartamentoNotifica(fichaGenericaForm.getNombreDepNotifica());
        
        if (fichaGenericaForm.getDepartamentoNotifica().equals("11")) {
        	this.setDepartamentoNotifica("CUNDINAMARCA");
        }
        
        this.setLugarNotifica(fichaGenericaForm.getLugarNotifica());
        this.setCodigoInstitucionAtendio("");
        this.setNombreEvento(fichaGenericaForm.getNombreEnfermedadNotificable());
        this.setCodigoDiagnostico(fichaGenericaForm.getCodigoDiagnostico());
        this.setNombreInstitucionAtendio(fichaGenericaForm.getNombreInstitucionAtendio());
        
        this.setFechaNotificacion("");
        this.setSemanaEpiNotificacion("");
        
        try {
	        this.setFechaNotificacion(fichaGenericaForm.getFechaNotificacion());
	        String semanaEpiNoti = Integer.toString(CalendarioEpidemiologico.obtenerNumeroSemana(fichaGenericaForm.getFechaNotificacion())[0]);
	        this.setSemanaEpiNotificacion(semanaEpiNoti);
        }
        catch (NullPointerException npe) {}
        
        String unidadesEdad = (String)CalendarioEpidemiologico.obtenerEdadDetallada(UtilidadFecha.conversionFormatoFechaAAp(fichaGenericaForm.getFechaNacimiento()),UtilidadFecha.getFechaActual()).elementAt(1);
        String edad = (String)CalendarioEpidemiologico.obtenerEdadDetallada(UtilidadFecha.conversionFormatoFechaAAp(fichaGenericaForm.getFechaNacimiento()),UtilidadFecha.getFechaActual()).elementAt(0);
        
        this.setEdad(edad);
        this.setUnidadesEdad(unidadesEdad);
        
        // Identificacion del paciente
        this.setPrimerNombre(fichaGenericaForm.getPrimerNombre());
        this.setSegundoNombre(fichaGenericaForm.getSegundoNombre());
        this.setPrimerApellido(fichaGenericaForm.getPrimerApellido());
        this.setSegundoApellido(fichaGenericaForm.getSegundoApellido());
        this.setDepartamentoNacimiento(fichaGenericaForm.getDepartamentoNacimiento());
        this.setCiudadNacimiento(fichaGenericaForm.getCiudadNacimiento());
        this.setDepartamentoResidencia(fichaGenericaForm.getDepartamentoResidencia());
        this.setCiudadResidencia(fichaGenericaForm.getCiudadResidencia());
        this.setDireccionPaciente(fichaGenericaForm.getDireccionPaciente());
        this.setTelefonoPaciente(fichaGenericaForm.getTelefonoPaciente());
        this.setFechaNacimiento(fichaGenericaForm.getFechaNacimiento());
        this.setGenero(fichaGenericaForm.getGenero());
        this.setEstadoCivil(fichaGenericaForm.getEstadoCivil());
        this.setDocumento(fichaGenericaForm.getDocumento());
        this.setBarrioResidencia(fichaGenericaForm.getBarrioResidencia());
        this.setZonaDomicilio(fichaGenericaForm.getZonaDomicilio());
        this.setOcupacion(fichaGenericaForm.getOcupacion());
        this.setAseguradora(fichaGenericaForm.getAseguradora());
        this.setRegimenSalud(fichaGenericaForm.getRegimenSalud());
        this.setEtnia(fichaGenericaForm.getEtnia());
        this.setDesplazado(fichaGenericaForm.getDesplazado());
        this.setTipoId(fichaGenericaForm.getTipoId());
        
        this.setCodCiudadResidencia(fichaGenericaForm.getCodCiudadResidencia());
        this.setCodDepResidencia(fichaGenericaForm.getCodDepResidencia());
        
        if (codDepResidencia.equals("11")) {
        	codDepResidencia = "25";
        }
        
        if (ocupacion.length()>30) {
        	ocupacion = ocupacion.substring(0,30);
        }
        if (etnia.equals("2")) {
        	etnia = "A";
        }
        else if (etnia.equals("4")) {
        	etnia = "I";
        }
        else {
        	etnia = "O";
        }
            
        
        // Notificacion
        this.setFechaConsultaGeneral(fichaGenericaForm.getFechaConsultaGeneral());
        this.setCodigoMunProcedencia(fichaGenericaForm.getCodigoMunProcedencia());
        this.setCodigoDepProcedencia(fichaGenericaForm.getCodigoDepProcedencia());
        this.setLugarProcedencia(fichaGenericaForm.getLugarProcedencia());
        this.setFechaInicioSint(fichaGenericaForm.getFechaInicioSint());
        this.setTipoCaso(fichaGenericaForm.getTipoCaso());
        this.setHospitalizado(fichaGenericaForm.isHospitalizado());
        this.setFechaHospitalizacion(fichaGenericaForm.getFechaHospitalizacion());
        this.setEstaVivo(fichaGenericaForm.isEstaVivo());
        this.setFechaDefuncion(fichaGenericaForm.getFechaDefuncion());
        this.setNombreProfesional(fichaGenericaForm.getNombreProfesional());
        
        this.setNombreMunProcedencia(fichaGenericaForm.getNombreMunProcedencia());
        this.setNombreDepProcedencia(fichaGenericaForm.getNombreDepProcedencia());
        
        if (fichaGenericaForm.getNombreDepProcedencia().equals("11")) {
        	this.setNombreDepProcedencia("CUNDINAMARCA");
        	this.setCodigoDepProcedencia("25");
        }
    }
    
    
    
    
    public void llenarCuadro(Graphics g,int x,int y)
    {
        g.setColor(Color.black);
        g.fillRect(x, y, 20, 20);
    }
    
        public String getNombreMunProcedencia()
        {
            return nombreMunProcedencia;
        }
        
        public void setNombreMunProcedencia(String nombreMunProcedencia)
        {
            this.nombreMunProcedencia = nombreMunProcedencia;
        }
        
        public String getNombreDepProcedencia() 
        {
            return nombreDepProcedencia;
        }
        
        public void setNombreDepProcedencia(String nombreDepProcedencia)
        {
            this.nombreDepProcedencia = nombreDepProcedencia;
        }
    
        public String getCodCiudadResidencia()
        {
            return codCiudadResidencia;
        }
        
        public void setCodCiudadResidencia(String codCiudadResidencia)
        {
            this.codCiudadResidencia = codCiudadResidencia;
        }
        
        public String getCodDepResidencia()
        {
            return codDepResidencia;
        }
        
        public void setCodDepResidencia(String codDepResidencia)
        {
            this.codDepResidencia = codDepResidencia;
        }
    
        public String getUnidadesEdad() {
            return unidadesEdad;
        }
        
        public void setUnidadesEdad(String unidadesEdad) {
            this.unidadesEdad = unidadesEdad;
        }
        
        public String getSemanaEpiNotificacion() {
            return semanaEpiNotificacion;
        }
        
        public void setSemanaEpiNotificacion(String semanaEpiNotificacion) {
            this.semanaEpiNotificacion = semanaEpiNotificacion;
        }
        
        public String getNombreInstitucionAtendio() {
            return nombreInstitucionAtendio;
        }
        
        public void setNombreInstitucionAtendio(String nombreInstitucionAtendio) {
            this.nombreInstitucionAtendio = nombreInstitucionAtendio;
        }
        
        public String getCodigoDiagnostico() {
            return codigoDiagnostico;
        }
        
        public void setCodigoDiagnostico(String codigoDiagnostico) {
            this.codigoDiagnostico = codigoDiagnostico;
        }
    
        public String getNombreEvento() {
            return nombreEvento;
        }
        
        
        public void setNombreEvento(String nombreEvento) {
            this.nombreEvento = nombreEvento;
        }
        
        
	public String getAseguradora() {
		return aseguradora;
	}


	public void setAseguradora(String aseguradora) {
		this.aseguradora = aseguradora;
	}


	public String getBarrioResidencia() {
		return barrioResidencia;
	}


	public void setBarrioResidencia(String barrioResidencia) {
		this.barrioResidencia = barrioResidencia;
	}


	public String getCiudadNacimiento() {
		return ciudadNacimiento;
	}


	public void setCiudadNacimiento(String ciudadNacimiento) {
		this.ciudadNacimiento = ciudadNacimiento;
	}


	public String getCiudadResidencia() {
		return ciudadResidencia;
	}


	public void setCiudadResidencia(String ciudadResidencia) {
		this.ciudadResidencia = ciudadResidencia;
	}


	public String getCodigoDepProcedencia() {
		return codigoDepProcedencia;
	}


	public void setCodigoDepProcedencia(String codigoDepProcedencia) {
		this.codigoDepProcedencia = codigoDepProcedencia;
	}


	public String getCodigoMunProcedencia() {
		return codigoMunProcedencia;
	}


	public void setCodigoMunProcedencia(String codigoMunProcedencia) {
		this.codigoMunProcedencia = codigoMunProcedencia;
	}


	public String getDepartamentoNacimiento() {
		return departamentoNacimiento;
	}


	public void setDepartamentoNacimiento(String departamentoNacimiento) {
		this.departamentoNacimiento = departamentoNacimiento;
	}


	public String getDepartamentoNotifica() {
		return departamentoNotifica;
	}


	public void setDepartamentoNotifica(String departamentoNotifica) {
		this.departamentoNotifica = departamentoNotifica;
	}


	public String getDepartamentoResidencia() {
		return departamentoResidencia;
	}


	public void setDepartamentoResidencia(String departamentoResidencia) {
		this.departamentoResidencia = departamentoResidencia;
	}


	public int getDesplazado() {
		return desplazado;
	}


	public void setDesplazado(int desplazado) {
		this.desplazado = desplazado;
	}


	public String getDireccionPaciente() {
		return direccionPaciente;
	}


	public void setDireccionPaciente(String direccionPaciente) {
		this.direccionPaciente = direccionPaciente;
	}


	public String getDocumento() {
		return documento;
	}


	public void setDocumento(String documento) {
		this.documento = documento;
	}


	public String getEdad() {
		return edad;
	}


	public void setEdad(String edad) {
		this.edad = edad;
	}


	public String getEstadoCivil() {
		return estadoCivil;
	}


	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}


	public boolean isEstaVivo() {
		return estaVivo;
	}


	public void setEstaVivo(boolean estaVivo) {
		this.estaVivo = estaVivo;
	}


	public String getEtnia() {
		return etnia;
	}


	public void setEtnia(String etnia) {
		this.etnia = etnia;
	}


	public String getFechaConsultaGeneral() {
		return fechaConsultaGeneral;
	}


	public void setFechaConsultaGeneral(String fechaConsultaGeneral) {
		this.fechaConsultaGeneral = fechaConsultaGeneral;
	}


	public String getFechaDefuncion() {
		return fechaDefuncion;
	}


	public void setFechaDefuncion(String fechaDefuncion) {
		this.fechaDefuncion = fechaDefuncion;
	}


	public String getFechaHospitalizacion() {
		return fechaHospitalizacion;
	}


	public void setFechaHospitalizacion(String fechaHospitalizacion) {
		this.fechaHospitalizacion = fechaHospitalizacion;
	}


	public String getFechaInicioSint() {
		return fechaInicioSint;
	}


	public void setFechaInicioSint(String fechaInicioSint) {
		this.fechaInicioSint = fechaInicioSint;
	}


	public String getFechaNacimiento() {
		return fechaNacimiento;
	}


	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}


	public String getFechaNotificacion() {
		return fechaNotificacion;
	}


	public void setFechaNotificacion(String fechaNotificacion) {
		this.fechaNotificacion = fechaNotificacion;
	}


	public String getGenero() {
		return genero;
	}


	public void setGenero(String genero) {
		this.genero = genero;
	}


	public boolean isHospitalizado() {
		return hospitalizado;
	}


	public void setHospitalizado(boolean hospitalizado) {
		this.hospitalizado = hospitalizado;
	}


	public String getCodigoInstitucionAtendio() {
		return codigoInstitucionAtendio;
	}


	public void setCodigoInstitucionAtendio(String codigoInstitucionAtendio) {
		this.codigoInstitucionAtendio = codigoInstitucionAtendio;
	}


	public String getLugarProcedencia() {
		return lugarProcedencia;
	}


	public void setLugarProcedencia(String lugarProcedencia) {
		this.lugarProcedencia = lugarProcedencia;
	}



	public String getMunicipioNotifica() {
		return municipioNotifica;
	}


	public void setMunicipioNotifica(String municipioNotifica) {
		this.municipioNotifica = municipioNotifica;
	}



	public String getNombreProfesional() {
		return nombreProfesional;
	}


	public void setNombreProfesional(String nombreProfesional) {
		this.nombreProfesional = nombreProfesional;
	}


	public String getOcupacion() {
		return ocupacion;
	}


	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}


	public String getPrimerApellido() {
		return primerApellido;
	}


	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}


	public String getPrimerNombre() {
		return primerNombre;
	}


	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}


	public String getRegimenSalud() {
		return regimenSalud;
	}


	public void setRegimenSalud(String regimenSalud) {
		this.regimenSalud = regimenSalud;
	}

	public String getSegundoApellido() {
		return segundoApellido;
	}


	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}


	public String getSegundoNombre() {
		return segundoNombre;
	}


	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}


	public String getSire() {
		return sire;
	}


	public void setSire(String sire) {
		this.sire = sire;
	}


	public String getTelefonoPaciente() {
		return telefonoPaciente;
	}


	public void setTelefonoPaciente(String telefonoPaciente) {
		this.telefonoPaciente = telefonoPaciente;
	}


	public int getTipoCaso() {
		return tipoCaso;
	}


	public void setTipoCaso(int tipoCaso) {
		this.tipoCaso = tipoCaso;
	}


	public String getTipoId() {
		return tipoId;
	}


	public void setTipoId(String tipoId) {
		this.tipoId = tipoId;
	}


	public String getZonaDomicilio() {
		return zonaDomicilio;
	}


	public void setZonaDomicilio(String zonaDomicilio) {
		this.zonaDomicilio = zonaDomicilio;
	}

	public String getLugarNotifica() {
		return lugarNotifica;
	}

	public void setLugarNotifica(String lugarNotifica) {
		this.lugarNotifica = lugarNotifica;
	}
}
