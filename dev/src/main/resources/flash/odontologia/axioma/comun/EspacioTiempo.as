package axioma.comun{
	
	import flash.display.Sprite;

	public class EspacioTiempo extends Sprite{

		private var consultorio:Consultorio;
		private var seleccionado:Boolean = false;
		private var asignado:Boolean = false;
		private var habilitado:Boolean = true;
		private var hora:String = "";
		private var horaFin:String = "";
		private var profesional:String = "";
		private var especialidad:String = "";
		private var codigoAgenda:int = 0;
		private var cita:XMLList;
		private var cupoExtra:Boolean = false;
		private var permiteAsignar:Boolean=false;
		private var permiteReservar:Boolean=false;
		private var permiteCupoExtra:Boolean=false;
		private var cuposExtraDisponibles:int=0;

		public function EspacioTiempo()
		{
		}
		
		public function setConsultorio(consultorio:Consultorio):void
		{
			this.consultorio=consultorio;
		}
		public function getConsultorio():Consultorio
		{
			return this.consultorio;
		}
		
		public function setSeleccionado(seleccionado:Boolean):void
		{
			if(this.habilitado)
			{
				this.seleccionado=seleccionado;
				if(this.seleccionado)
				{
					this.alpha=0.1;
				}
				else{
					if(asignado)
					{
						this.alpha=0.5;
					}
					else
					{
						this.alpha=0.9;
					}
				}
			}
		}
		
		public function getSeleccionado():Boolean
		{
			return this.seleccionado;
		}

		public function setAsignado(asignado:Boolean):void
		{
			this.asignado=asignado;
		}
		
		public function getAsignado():Boolean
		{
			return this.asignado;
		}
		public function getX():int
		{
			var objeto:Object=this;
			var x=objeto.x;
			while(objeto.parent!=null)
			{
				objeto=objeto.parent;
				x+=objeto.x;
			}
			return x;
		}
		public function getY():int
		{
			var objeto:Object=this;
			var y=objeto.y;
			while(objeto.parent!=null)
			{
				objeto=objeto.parent;
				y+=objeto.y;
			}
			return y;
		}
		public function seleccionar():void
		{
			if(this.habilitado)
			{
				this.setSeleccionado(true);
			}
		}
		
		public function setHabilitado(habilitado:Boolean):void
		{
			this.habilitado=habilitado;
		}
		public function getHabilitado():Boolean
		{
			return habilitado;
		}

		public function setHora(hora:String)
		{
			this.hora=hora;
		}
		public function getHora()
		{
			return this.hora;
		}

		public function setHoraFin(horaFin:String)
		{
			this.horaFin=horaFin;
		}
		public function getHoraFin()
		{
			return this.horaFin;
		}
		
		public function getProfesional():String
		{
			return this.profesional;
		}
		public function setProfesional(profesional:String):void
		{
			this.profesional=profesional;
		}


		public function getEspecialidad():String
		{
			return this.especialidad;
		}
		public function setEspecialidad(especialidad:String):void
		{
			this.especialidad=especialidad;
		}

		public function getCodigoAgenda():int
		{
			return this.codigoAgenda;
		}
		public function setCodigoAgenda(codigoAgenda:int):void
		{
			this.codigoAgenda=codigoAgenda;;
		}

		public function getCita():XMLList
		{
			return this.cita;
		}
		public function setCita(cita:XMLList):void
		{
			this.cita=cita;
		}

		public function setCupoExtra(cupoExtra:Boolean):void
		{
			this.cupoExtra=cupoExtra;
		}
		public function getCupoExtra():Boolean
		{
			return cupoExtra;
		}

		public function setPermiteAsignar(permiteAsignar:Boolean):void
		{
			this.permiteAsignar=permiteAsignar;
		}
		public function getPermiteAsignar():Boolean
		{
			return permiteAsignar;
		}

		public function setPermiteReservar(permiteReservar:Boolean):void
		{
			this.permiteReservar=permiteReservar;
		}
		public function getPermiteReservar():Boolean
		{
			return permiteReservar;
		}

		public function setPermiteCupoExtra(permiteCupoExtra:Boolean):void
		{
			this.permiteCupoExtra=permiteCupoExtra;
		}
		public function getPermiteCupoExtra():Boolean
		{
			return this.permiteCupoExtra;
		}
		public function setCuposExtraDisponibles(cuposExtraDisponibles:int):void
		{
			this.cuposExtraDisponibles=cuposExtraDisponibles;
		}
		public function getCuposExtraDisponibles():int
		{
			return this.cuposExtraDisponibles;
		}
	}
}