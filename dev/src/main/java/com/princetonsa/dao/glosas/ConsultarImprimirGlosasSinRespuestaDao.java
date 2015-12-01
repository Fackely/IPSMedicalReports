package com.princetonsa.dao.glosas;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.glosas.DtoGlosa;


public interface ConsultarImprimirGlosasSinRespuestaDao {

public ArrayList<DtoGlosa> consultarListadoGlosas(Connection con, String filtroConvenio, String filtroContrato, String fechaInicial, String fechaFinal, String indicativo, String consecutivoFactura);

public String cadenaConsultaGlosasSinResp(HashMap parametros);

public boolean guardar(Connection con, HashMap criterios);



  
  


}
