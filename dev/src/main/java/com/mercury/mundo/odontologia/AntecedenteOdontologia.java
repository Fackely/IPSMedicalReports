package com.mercury.mundo.odontologia;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;

import util.InfoDatos;
import util.ResultadoBoolean;
import util.UtilidadFecha;

import com.mercury.dao.odontologia.AntecedentesOdontologiaDao;
import com.princetonsa.dao.DaoFactory;

public class AntecedenteOdontologia
{
    private int codPaciente;
    private String observaciones;
    private ArrayList habitos;
    private ArrayList habitosOtros;
    private ArrayList traumatismos;
    private ArrayList traumatismosOtros;
    private ArrayList tratamientosPrevios;
    
    private static AntecedentesOdontologiaDao antecedentesodontologia;
    
    private static AntecedentesOdontologiaDao getAntecedentesOdontologiaDao()
    {
        if(antecedentesodontologia==null)
        {
            String tipoBD = System.getProperty("TIPOBD" );
            DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
            antecedentesodontologia=myFactory.getAntecedentesOdontologiaDao();
        }
        return antecedentesodontologia;
    }
    
    public AntecedenteOdontologia()
    {
        this.habitos=new ArrayList();
        this.habitosOtros=new ArrayList();
        this.traumatismos=new ArrayList();
        this.traumatismosOtros=new ArrayList();
        this.tratamientosPrevios=new ArrayList();
        clean();
    }
    
    public void clean()
    {
        this.codPaciente=-1;
        this.observaciones="";
        this.habitos.clear();
        this.habitosOtros.clear();
        this.traumatismos.clear();
        this.traumatismosOtros.clear();
        this.tratamientosPrevios.clear();
    }
    
    public int getCodPaciente()
    {
        return this.codPaciente;
    }
    
    public void setCodPaciente(int codPaciente)
    {
        this.codPaciente=codPaciente;
    }
    
    public String getObservaciones()
    {
        return this.observaciones;
    }
    
    public void setObservaciones(String observaciones)
    {
        this.observaciones=observaciones;
    }
    
    public void addHabito(InfoDatos habito)
    {
        this.habitos.add(habito);
    }
    
    public InfoDatos getHabito(int i)
    {
        return (InfoDatos)this.habitos.get(i);
    }
    
    public int getNumHabitos()
    {
        return this.habitos.size();
    }

    public void addHabitoOtro(InfoDatos habitoOtro)
    {
        this.habitosOtros.add(habitoOtro);
    }
    
    public InfoDatos getHabitoOtro(int i)
    {
        return (InfoDatos)this.habitosOtros.get(i);
    }
    
    public int getNumHabitosOtros()
    {
        return this.habitosOtros.size();
    }

    public void addTraumatismo(InfoDatos traumatismo)
    {
        this.traumatismos.add(traumatismo);
    }
    
    public InfoDatos getTraumatismo(int i)
    {
        return (InfoDatos)this.traumatismos.get(i);
    }
    
    public int getNumTraumatismos()
    {
        return this.traumatismos.size();
    }

    public void addTraumatismoOtro(InfoDatos traumatismoOtro)
    {
        this.traumatismosOtros.add(traumatismoOtro);
    }
    
    public InfoDatos getTraumatismoOtro(int i)
    {
        return (InfoDatos)this.traumatismosOtros.get(i);
    }
    
    public int getNumTraumatismosOtros()
    {
        return this.traumatismosOtros.size();
    }

    public void addTratamientoPrevio(TratamientoPrevio tratamiento)
    {
        this.tratamientosPrevios.add(tratamiento);
    }
    
    public TratamientoPrevio getTratamientoPrevio(int i)
    {
        return (TratamientoPrevio)this.tratamientosPrevios.get(i);
    }
    
    public int getNumTratamientosPrevios()
    {
        return this.tratamientosPrevios.size();
    }
    
    public ResultadoBoolean guardar(Connection con)
    {
        boolean inicioTransaccion=false;
        DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));        

        try
        {
            if(!AntecedenteOdontologia.getAntecedentesOdontologiaDao().existe(con, this.getCodPaciente()))
            {
                return this.insertar(con);
            }
            else
            {
                inicioTransaccion = myFactory.beginTransaction(con);
                if(inicioTransaccion)
                {
                    AntecedenteOdontologia.getAntecedentesOdontologiaDao().modificar(con, this.getCodPaciente(),this.getObservaciones());

                    for(int i=0; i<this.getNumHabitos(); i++)
                    {
                        InfoDatos habito = this.getHabito(i);
                        if(!AntecedenteOdontologia.getAntecedentesOdontologiaDao().existeHabito(con, this.getCodPaciente(), habito.getCodigo()))
                        {
                            AntecedenteOdontologia.getAntecedentesOdontologiaDao().insertarHabito(
                                    con, this.getCodPaciente(), habito.getCodigo(), habito.getDescripcion());
                        }
                        else
                        {
                            AntecedenteOdontologia.getAntecedentesOdontologiaDao().modificarHabito(
                                    con, this.getCodPaciente(), habito.getCodigo(), habito.getDescripcion());
                        }
                    }

                    for(int i=0; i<this.getNumHabitosOtros(); i++)
                    {
                        InfoDatos habitoOtro = this.getHabitoOtro(i);
                        if(!AntecedenteOdontologia.getAntecedentesOdontologiaDao().existeHabitoOtro(con, this.getCodPaciente(), habitoOtro.getCodigo()))
                        {
                            AntecedenteOdontologia.getAntecedentesOdontologiaDao().insertarHabitoOtro(
                                    con, this.getCodPaciente(), habitoOtro.getCodigo(), 
                                    habitoOtro.getNombre(), habitoOtro.getDescripcion());
                        }
                        else
                        {
                            AntecedenteOdontologia.getAntecedentesOdontologiaDao().modificarHabitoOtro(
                                    con, this.getCodPaciente(), habitoOtro.getCodigo(), habitoOtro.getDescripcion());
                        }
                    }
                    for(int i=0; i<this.getNumTraumatismos(); i++)
                    {
                        InfoDatos traumatismo = this.getTraumatismo(i);
                        if(!AntecedenteOdontologia.getAntecedentesOdontologiaDao().existeTraumatismo(con, this.getCodPaciente(), traumatismo.getCodigo()))
                        {
                            AntecedenteOdontologia.getAntecedentesOdontologiaDao().insertarTraumatismo(
                                    con, this.getCodPaciente(), traumatismo.getCodigo(), traumatismo.getDescripcion());
                        }
                        else
                        {
                            AntecedenteOdontologia.getAntecedentesOdontologiaDao().modificarTraumatismo(
                                    con, this.getCodPaciente(), traumatismo.getCodigo(), traumatismo.getDescripcion());
                        }
                    }
                    for(int i=0; i<this.getNumTraumatismosOtros(); i++)
                    {
                        InfoDatos traumatismoOtro = this.getTraumatismoOtro(i);
                        if(!AntecedenteOdontologia.getAntecedentesOdontologiaDao().existeTraumatismoOtro(con, this.getCodPaciente(), traumatismoOtro.getCodigo()))
                        {
                            AntecedenteOdontologia.getAntecedentesOdontologiaDao().insertarTraumatismoOtro(
                                    con, this.getCodPaciente(), traumatismoOtro.getCodigo(), 
                                    traumatismoOtro.getNombre(), traumatismoOtro.getDescripcion());
                        }
                        else
                        {
                            AntecedenteOdontologia.getAntecedentesOdontologiaDao().modificarTraumatismoOtro(
                                    con, this.getCodPaciente(), traumatismoOtro.getCodigo(), traumatismoOtro.getDescripcion());
                        }
                    }
                    for(int i=0; i<this.getNumTratamientosPrevios(); i++)
                    {
                        TratamientoPrevio tratamientoPrevio = this.getTratamientoPrevio(i);
                        if(!AntecedenteOdontologia.getAntecedentesOdontologiaDao().existeTratamientoPrevio(con, this.getCodPaciente(), tratamientoPrevio.getCodigo()))
                        {
                            AntecedenteOdontologia.getAntecedentesOdontologiaDao().insertarTratamientoPrevio(
                                    con, this.getCodPaciente(), tratamientoPrevio.getCodigo(), 
                                    tratamientoPrevio.getTipoTratamiento(), tratamientoPrevio.getFechaInicio(),
                                    tratamientoPrevio.getFechaFinalizacion(), tratamientoPrevio.getDescripcion());
                        }
                        else
                        {
                            AntecedenteOdontologia.getAntecedentesOdontologiaDao().modificarTratamientoPrevio(
                                    con, this.getCodPaciente(), tratamientoPrevio.getCodigo(), 
                                    tratamientoPrevio.getFechaInicio(), tratamientoPrevio.getFechaFinalizacion(), 
                                    tratamientoPrevio.getDescripcion());
                        }
                    }

                    myFactory.endTransaction(con);
                    
                    return new ResultadoBoolean(true, "Se actualizaron exitosamente antecedentes de odontologia para paciente: "+this.getCodPaciente());
                }
                else
                {
                    return new ResultadoBoolean(false, "No se pudo iniciar la transacción");
                }
            }
        }
        catch(SQLException e)
        {
            try
            {
                if(inicioTransaccion)
                    myFactory.abortTransaction(con);
            }
            catch(SQLException se)
            {   
            }
            return new ResultadoBoolean(false, "Error insertando antecedentes de odontologia para paciente: "+this.getCodPaciente());
        }
    }

    private ResultadoBoolean insertar(Connection con)
    {
        boolean inicioTransaccion=false;
        DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));        
        
        try
        {
            inicioTransaccion = myFactory.beginTransaction(con);
            if(inicioTransaccion)
            {
                if(!AntecedenteOdontologia.getAntecedentesOdontologiaDao().existenAntecedentes(con, this.getCodPaciente()))
                {
                    AntecedenteOdontologia.getAntecedentesOdontologiaDao().insertarAntecedenteGeneral(con, this.getCodPaciente());
                }
                AntecedenteOdontologia.getAntecedentesOdontologiaDao().insertar(
                        con, 
                        this.getCodPaciente(),
                        this.getObservaciones());
                for(int i=0; i<this.getNumHabitos(); i++)
                {
                    InfoDatos habito = this.getHabito(i);
                    AntecedenteOdontologia.getAntecedentesOdontologiaDao().insertarHabito(
                            con, this.getCodPaciente(), habito.getCodigo(), habito.getDescripcion());
                }
                for(int i=0; i<this.getNumHabitosOtros(); i++)
                {
                    InfoDatos habitoOtro = this.getHabitoOtro(i);
                    AntecedenteOdontologia.getAntecedentesOdontologiaDao().insertarHabitoOtro(
                            con, this.getCodPaciente(), habitoOtro.getCodigo(), 
                            habitoOtro.getNombre(), habitoOtro.getDescripcion());
                }
                for(int i=0; i<this.getNumTraumatismos(); i++)
                {
                    InfoDatos traumatismo = this.getTraumatismo(i);
                    AntecedenteOdontologia.getAntecedentesOdontologiaDao().insertarTraumatismo(
                            con, this.getCodPaciente(), traumatismo.getCodigo(), traumatismo.getDescripcion());
                }
                for(int i=0; i<this.getNumTraumatismosOtros(); i++)
                {
                    InfoDatos traumatismoOtro = this.getTraumatismoOtro(i);
                    AntecedenteOdontologia.getAntecedentesOdontologiaDao().insertarTraumatismoOtro(
                            con, this.getCodPaciente(), traumatismoOtro.getCodigo(), 
                            traumatismoOtro.getNombre(), traumatismoOtro.getDescripcion());
                }
                for(int i=0; i<this.getNumTratamientosPrevios(); i++)
                {
                    TratamientoPrevio tratamientoPrevio = this.getTratamientoPrevio(i);
                    AntecedenteOdontologia.getAntecedentesOdontologiaDao().insertarTratamientoPrevio(
                            con, this.getCodPaciente(), tratamientoPrevio.getCodigo(), 
                            tratamientoPrevio.getTipoTratamiento(), tratamientoPrevio.getFechaInicio(),
                            tratamientoPrevio.getFechaFinalizacion(), tratamientoPrevio.getDescripcion());
                }
                
                myFactory.endTransaction(con);
                    
                return new ResultadoBoolean(true, "Se adicionaron exitosamente antecedentes de odontologia para paciente: "+this.getCodPaciente());
            }
            else
            {
                return new ResultadoBoolean(false, "No se pudo iniciar la transacción");
            }
        }
        catch(SQLException e)
        {
            try
            {
                if(inicioTransaccion)
                    myFactory.abortTransaction(con);
            }
            catch(SQLException se)
            {   
            }
            return new ResultadoBoolean(false, "Error insertando antecedentes de odontologia para paciente: "+this.getCodPaciente());
        }
    }

    public ResultadoBoolean consultar(Connection con, int codPaciente)
    {
        try
        {
            ResultSetDecorator rs=AntecedenteOdontologia.getAntecedentesOdontologiaDao().consultar(con, codPaciente);
            if(rs.next())
            {
                this.setCodPaciente(codPaciente);
                this.setObservaciones(rs.getString("observaciones"));
                
                ResultSetDecorator rsHabitos = AntecedenteOdontologia.getAntecedentesOdontologiaDao().consultarHabitos(con, codPaciente);
                while(rsHabitos.next())
                {
                    InfoDatos habito = new InfoDatos(
                            rsHabitos.getInt("codTipoHabitoOdo"),
                            rsHabitos.getString("nomTipoHabitoOdo"),
                            rsHabitos.getString("observaciones"));
                    this.addHabito(habito);
                }
                
                ResultSetDecorator rsHabitosOtros = AntecedenteOdontologia.getAntecedentesOdontologiaDao().consultarHabitosOtros(con, codPaciente);
                while(rsHabitosOtros.next())
                {
                    InfoDatos habitoOtro = new InfoDatos(
                            rsHabitosOtros.getInt("codigo"),
                            rsHabitosOtros.getString("nombre"),
                            rsHabitosOtros.getString("observaciones"));
                    this.addHabitoOtro(habitoOtro);
                }
                ResultSetDecorator rsTraumatismos = AntecedenteOdontologia.getAntecedentesOdontologiaDao().consultarTraumatismos(con, codPaciente);
                while(rsTraumatismos.next())
                {
                    InfoDatos traumatismo = new InfoDatos(
                            rsTraumatismos.getInt("codTipoTraumatismoOdo"),
                            rsTraumatismos.getString("nomTipoTraumatismoOdo"),
                            rsTraumatismos.getString("observaciones"));
                    this.addTraumatismo(traumatismo);
                }
                
                ResultSetDecorator rsTraumatismosOtros = AntecedenteOdontologia.getAntecedentesOdontologiaDao().consultarTraumatismosOtros(con, codPaciente);
                while(rsTraumatismosOtros.next())
                {
                    InfoDatos traumatismoOtro = new InfoDatos(
                            rsTraumatismosOtros.getInt("codigo"),
                            rsTraumatismosOtros.getString("nombre"),
                            rsTraumatismosOtros.getString("observaciones"));
                    this.addTraumatismoOtro(traumatismoOtro);
                }

                ResultSetDecorator rsTratamientosPrevios = AntecedenteOdontologia.getAntecedentesOdontologiaDao().consultarTratamientosPrevios(con, codPaciente);
                while(rsTratamientosPrevios.next())
                {
                    TratamientoPrevio tratamientoPrevio = new TratamientoPrevio(
                            rsTratamientosPrevios.getInt("codigo"),
                            rsTratamientosPrevios.getString("tipoTratamiento"),
                            UtilidadFecha.conversionFormatoFechaAAp(rsTratamientosPrevios.getString("fechaInicio")),
                            UtilidadFecha.conversionFormatoFechaAAp(rsTratamientosPrevios.getString("fechaFinalizacion")),
                            rsTratamientosPrevios.getString("descripcion"));
                    this.addTratamientoPrevio(tratamientoPrevio);
                }

                return new ResultadoBoolean(true, "Se cargó exitosamente los antecendentes del paciente: "+codPaciente);
            }
            else
            {
                return new ResultadoBoolean(false, "No existen antecedentes para el paciente: " + codPaciente + " consultarAntecedenteOdontologia ");
            }
        }
        catch (SQLException e)
        {
            return new ResultadoBoolean(false, "Se presentó error cargando antecedente odontologia para el paciente: " + codPaciente + " consultarAntecedenteOdontologia " + e.toString());
        }
    }
}
