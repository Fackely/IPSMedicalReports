/*
 * Creado en 21-feb-2006
 * 
 * Autor Santiago Salamanca
 * SysMédica
 */
package com.sysmedica.mundo.saludpublica;

/**
 * @author santiago
 *
 */
public class Paciente {

    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String departamentoNacimiento;
    private String ciudadNacimiento;
    private String departamentoResidencia;
    private String ciudadResidencia;
    private String direccion;
    private String telefono;
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
    private String grupoPoblacional;
    
    private String paisExpedicion;
    private String paisNacimiento;
    private String paisResidencia;
    
    
    /**
     * @return Returns the ciudadNacimiento.
     */
    public String getCiudadNacimiento() {
        return ciudadNacimiento;
    }
    /**
     * @param ciudadNacimiento The ciudadNacimiento to set.
     */
    public void setCiudadNacimiento(String ciudadNacimiento) {
        this.ciudadNacimiento = ciudadNacimiento;
    }
    /**
     * @return Returns the ciudadResidencia.
     */
    public String getCiudadResidencia() {
        return ciudadResidencia;
    }
    /**
     * @param ciudadResidencia The ciudadResidencia to set.
     */
    public void setCiudadResidencia(String ciudadResidencia) {
        this.ciudadResidencia = ciudadResidencia;
    }
    /**
     * @return Returns the departamentoNacimiento.
     */
    public String getDepartamentoNacimiento() {
        return departamentoNacimiento;
    }
    /**
     * @param departamentoNacimiento The departamentoNacimiento to set.
     */
    public void setDepartamentoNacimiento(String departamentoNacimiento) {
        this.departamentoNacimiento = departamentoNacimiento;
    }
    /**
     * @return Returns the departamentoResidencia.
     */
    public String getDepartamentoResidencia() {
        return departamentoResidencia;
    }
    /**
     * @param departamentoResidencia The departamentoResidencia to set.
     */
    public void setDepartamentoResidencia(String departamentoResidencia) {
        this.departamentoResidencia = departamentoResidencia;
    }
    /**
     * @return Returns the direccion.
     */
    public String getDireccion() {
        return direccion;
    }
    /**
     * @param direccion The direccion to set.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    /**
     * @return Returns the documento.
     */
    public String getDocumento() {
        return documento;
    }
    /**
     * @param documento The documento to set.
     */
    public void setDocumento(String documento) {
        this.documento = documento;
    }
    /**
     * @return Returns the edad.
     */
    public String getEdad() {
        return edad;
    }
    /**
     * @param edad The edad to set.
     */
    public void setEdad(String edad) {
        this.edad = edad;
    }
    /**
     * @return Returns the estadoCivil.
     */
    public String getEstadoCivil() {
        return estadoCivil;
    }
    /**
     * @param estadoCivil The estadoCivil to set.
     */
    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }
    /**
     * @return Returns the fechaNacimiento.
     */
    public String getFechaNacimiento() {
        return fechaNacimiento;
    }
    /**
     * @param fechaNacimiento The fechaNacimiento to set.
     */
    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    /**
     * @return Returns the genero.
     */
    public String getGenero() {
        return genero;
    }
    /**
     * @param genero The genero to set.
     */
    public void setGenero(String genero) {
        this.genero = genero;
    }
    /**
     * @return Returns the primerApellido.
     */
    public String getPrimerApellido() {
        return primerApellido;
    }
    /**
     * @param primerApellido The primerApellido to set.
     */
    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }
    /**
     * @return Returns the primerNombre.
     */
    public String getPrimerNombre() {
        return primerNombre;
    }
    /**
     * @param primerNombre The primerNombre to set.
     */
    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }
    /**
     * @return Returns the segundoApellido.
     */
    public String getSegundoApellido() {
        return segundoApellido;
    }
    /**
     * @param segundoApellido The segundoApellido to set.
     */
    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }
    /**
     * @return Returns the segundoNombre.
     */
    public String getSegundoNombre() {
        return segundoNombre;
    }
    /**
     * @param segundoNombre The segundoNombre to set.
     */
    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }
    /**
     * @return Returns the telefono.
     */
    public String getTelefono() {
        return telefono;
    }
    /**
     * @param telefono The telefono to set.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
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
	public int isDesplazado() {
		return desplazado;
	}
	public void setDesplazado(int desplazado) {
		this.desplazado = desplazado;
	}
	public String getEtnia() {
		return etnia;
	}
	public void setEtnia(String etnia) {
		this.etnia = etnia;
	}
	public String getOcupacion() {
		return ocupacion;
	}
	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}
	public String getRegimenSalud() {
		return regimenSalud;
	}
	public void setRegimenSalud(String regimenSalud) {
		this.regimenSalud = regimenSalud;
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
	public String getGrupoPoblacional() {
		return grupoPoblacional;
	}
	public void setGrupoPoblacional(String grupoPoblacional) {
		this.grupoPoblacional = grupoPoblacional;
	}
	public String getPaisExpedicion() {
		return paisExpedicion;
	}
	public void setPaisExpedicion(String paisExpedicion) {
		this.paisExpedicion = paisExpedicion;
	}
	public String getPaisNacimiento() {
		return paisNacimiento;
	}
	public void setPaisNacimiento(String paisNacimiento) {
		this.paisNacimiento = paisNacimiento;
	}
	public String getPaisResidencia() {
		return paisResidencia;
	}
	public void setPaisResidencia(String paisResidencia) {
		this.paisResidencia = paisResidencia;
	}
}
