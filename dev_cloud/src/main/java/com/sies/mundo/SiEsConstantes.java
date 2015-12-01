package com.sies.mundo;

public class SiEsConstantes
{
	public static final String MANANA="M";
	public static final String TARDE="T";
	public static final String NOCHE="N";
	public static final String CORRIDO="C";
	public static final String NOVEDAD="P";
	public static final String DESCANSO="D";
	public static final String VACACIONES="V";
	public static final String LIBRE="L";
	public static final String NOTURNO="_";
	
	/*-----------------------------CODIGOS RESTRICCIONES CUADRO DE TURNOS-----------------------*/
	public static final int codigoRestriccionNN=1;//Una enfermera puede hacer dos noches seguidas
	public static final int codigoRestriccionMinM=2;//Minimo de enfermeras en la Mañana dia Ordinario
	public static final int codigoRestriccionMinT=3;//Minimo de enfermeras en la tarde dia Ordinario
	public static final int codigoRestriccionesEnfN=4;//Minimo de enfermeras en la Noche dia Ordinario
	public static final int codigoRestriccionMinMFestivo=5;//Minimo de enfermeras en la Mañana dia Festivo
	public static final int codigoRestriccionMinTFestivo=6;//Minimo de enfermeras en la tarde dia Festivo
	public static final int codigoRestriccionMinNFestivo=7;//Minimo de enfermeras en la Noche dia Festivo
	public static final int codigoRestriccionCC=8;//Una enfermera puede hacer dos corridos seguidos
	public static final int codigoRestriccionFinDeSemanaLibre=9;//El cuadro de turnos es nocturno
	public static final int codigoRestriccionNumeroCorridosSemana=10;//Se puede saltar la restricción de asignar libre después de noche
	
	public static final int restriccionEnfLunes=1;
	public static final int restriccionEnfMartes=2;
	public static final int restriccionEnfMiercoles=3;
	public static final int restriccionEnfJueves=4;
	public static final int restriccionEnfViernes=5;
	public static final int restriccionEnfSabado=6;
	public static final int restriccionEnfDomingo=7;
	public static final int restriccionEnfFestivos=8;
	public static final int restriccionEnfHorasSemanales=9;
	public static final int restriccionEnfDominicales=10;

	// Para consultar los listados de las restricciones
	public static final int TIPO_RESTRICCION_ENFERMERA=1;
	public static final int TIPO_RESTRICCION_CATEGORIA=2;
	
	//Manejo de turnos en días festivos, ordinarios o para todos los días 
	public static final int TURNO_TODOS_LOS_DIAS=1;
	public static final int TURNO_ORDINARIO=2;
	public static final int TURNO_FESTIVO=3;
	public static final int TURNO_ORDINARIO_SABADO=4;
	public static final int TURNO_SABADO_FESTIVO=5;
	public static final int TURNO_SABADO=6;
	
	// Días de la semana
	public static final int DIA_SEMANA_LUNES=0;
	public static final int DIA_SEMANA_MARTES=1;
	public static final int DIA_SEMANA_MIERCOLES=2;
	public static final int DIA_SEMANA_JUEVES=3;
	public static final int DIA_SEMANA_VIERNES=4;
	public static final int DIA_SEMANA_SABADO=5;
	public static final int DIA_SEMANA_DOMINGO=6;

	// Contadores
	public static final String CONTADOR_HORAS="contador_horas_trabajadas_";
	public static final String CONTADOR_CORRIDOS_FESTIVOS="contador_corridos_festivos_";
	public static final String CONTADOR_CORRIDOS="contador_corridos_";
	public static final String CONTADOR_CORRIDOS_ORDINARIOS="contador_corridos_ordinarios_";
	public static final String CONTADOR_MANANAS="cantManana_";
	public static final String CONTADOR_TARDES="cantTarde_";
	public static final String CONTADOR_NOCHES="cantNoche_";
	public static final String CONTADOR_MANANAS_ENF="cantMananaEnf_";
	public static final String CONTADOR_TARDES_ENF="cantTardeEnf_";
	public static final String CONTADOR_NOCHES_ENF="cantNocheEnf_";
	public static final String CONTADOR_NOCHES_ENF_FESTIVO="cantNocheEnf_festivo_";
	public static final String CONTADOR_NOCHES_ENF_ORDINARIO="cantNocheEnf_ordinario_";
	public static final String CONTADOR_LIBRES_PERSONA="contador_libres_persona_";
	public static final String CONTADOR_HORAS_SEMANA="horas_semana_";
	public static final String CONTADOR_SEMANAS_INVOLUCRADAS="semanas_involucradas_";
	public static final String CONTADOR_HORAS_EXTRAS="horas_extras_";
	public static final String CONTADOR_DIAS_HABILES_CUADRO="dias_habiles_";
	public static final String CONTADOR_INCONSISTENCIAS_MANANAS="inconsistencia_mananas_";
	public static final String CONTADOR_INCONSISTENCIAS_TARDES="inconsistencia_tardes_";
	public static final String CONTADOR_INCONSISTENCIAS_NOCHES="inconsistencia_noches_";
	public static final String CONTADOR_FINES_DE_SEMANA_PERSONA="fines_de_semana_persona_";
	public static final String CONTADOR_HORAS_FALTANTES="horas_faltantes_";
	
	// Valores máximos de los corridos
	public static final int MAXIMO_CORRIDOS_MES=6;
	public static final int MAXIMO_CORRIDOS_SEMANA=3;
	public static final int MAXIMO_CORRIDOS_2_SEMANAS=6;
	
	public static final int NUMERO_DOMINICALES_MES=2;
	public static final int NUMERO_HORAS_SEMANALES=48;
	public static final int NUMERO_HORAS_LABORALES_DIARIAS=8;
	public static final int JORNADA_MAXIMA_SEMANAL=60;
	public static final int MAXIMO_NOCHES_SEMANA=4;
	public static final int MAXIMO_NOCHES_SEMANA_CON_NOCHE_SEGUIDA=5;
	public static final int MAXIMO_TURNOS_SEMANA_TOTAL_HORAS=7;
	public static final int MINIMO_LIBRES_MES=4;
	public static final int NUMERO_DIAS_MES=30;
	
	public static final String KEY_CODIGO_ENFERMERA="codigoEnfermera_";
	public static final String KEY_NOMBRE_ENFERMERA="nombreEnfermera_";
	public static final String KEY_TELEFONO_ENFERMERA="telefonoEnfermera_";
	public static final String KEY_TURNO="turno_";
	public static final String KEY_TIENE_CUADRO_ANTERIOR="tiene_cuadro_anterior_";
	public static final String KEY_PERMITIR_MODIFICAR="permitir_modificar_";
	public static final String KEY_FECHA_INICIO="fecha_inicio_persona_";
	public static final String KEY_FECHA_FIN="fecha_fin_persona_";
	public static final String KEY_HORAS_FALTANTES_ANTERIORES="horas_faltantes_anteriores_";
	public static final String KEY_HORAS_CONTRATADAS="horas_contratadas_";
	public static final String KEY_PERSONA_PROCESADA_SECUENCIA="persona_procesada_";
	
	public static final int DIFERENCIA_IDEAL_HORAS=6;
	public static final int CICLOS_VERIFICACION=1;
	public static final int CICLOS_VERIFICACION_EQUILIBRIO=20;
	
	public static final int OPCION_CAMBIO_TURNO=1;
	public static final int OPCION_REGISTRO_OBSERVACION=2;
	public static final int OPCION_REGISTRO_NOVEDAD=3;
	public static final int OPCION_IMPRIMIR_OBSERVACIONES=4;
	public static final int OPCION_ELIMINAR_TURNO=5;
	public static final int OPCION_CUBRIR_TURNO=6;
	public static final int OPCION_MODIFICACION_HORAS=7;
	
	public static final int CODIGO_TIPO_OBSERVACION_CAMBIO_TURNO=1;
	public static final int CODIGO_TIPO_OBSERVACION_REGISTRO_NOVEDAD=2;
	public static final int CODIGO_TIPO_OBSERVACION_CUBRIR_TURNO=3;
	public static final int CODIGO_TIPO_OBSERVACION_MODIFICAR_HORAS=4;
	
	public static final short FORMATO_ARCHIVO_PDF=1;
	public static final short FORMATO_ARCHIVO_EXCEL=2;

	public static final int SEXO_TODOS = 0;
	public static final int SEXO_MASCULINO = 1;
	public static final int SEXO_FEMENINO = 2;
	
	public static final int TIPO_CUADRO_ALEATORIO_CORRIDOS=1;
	public static final int TIPO_CUADRO_ALEATORIO_TARDES=2;
	public static final int TIPO_CUADRO_SECUENCIAL=3;
	public static final int TIPO_CUADRO_ROTATIVO=4;
}
