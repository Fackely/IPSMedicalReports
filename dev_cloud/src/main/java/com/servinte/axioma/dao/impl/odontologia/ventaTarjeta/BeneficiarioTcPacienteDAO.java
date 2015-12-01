package com.servinte.axioma.dao.impl.odontologia.ventaTarjeta;

import com.servinte.axioma.dao.interfaz.odontologia.ventaTarjeta.IBeneficiarioTcPacienteDAO;
import com.servinte.axioma.orm.BeneficiarioTcPaciente;
import com.servinte.axioma.orm.delegate.odontologia.ventaTarjeta.BeneficiarioTcPacienteDelegate;

public class BeneficiarioTcPacienteDAO implements IBeneficiarioTcPacienteDAO{

	@Override
	public BeneficiarioTcPaciente buscarxId(Number id) {
		BeneficiarioTcPacienteDelegate delegate=new BeneficiarioTcPacienteDelegate();
		return delegate.findById(id.longValue());
	}

	@Override
	public void eliminar(BeneficiarioTcPaciente beneficiarioTcPaciente) {
		BeneficiarioTcPacienteDelegate delegate=new BeneficiarioTcPacienteDelegate();
		delegate.delete(beneficiarioTcPaciente);
	}

	@Override
	public void insertar(BeneficiarioTcPaciente beneficiarioTcPaciente) {
		BeneficiarioTcPacienteDelegate delegate=new BeneficiarioTcPacienteDelegate();
		delegate.persist(beneficiarioTcPaciente);
	}

	@Override
	public void modificar(BeneficiarioTcPaciente beneficiarioTcPaciente) {
		BeneficiarioTcPacienteDelegate delegate=new BeneficiarioTcPacienteDelegate();
		delegate.attachDirty(beneficiarioTcPaciente);
		
	}

}
