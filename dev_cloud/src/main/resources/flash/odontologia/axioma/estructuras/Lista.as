package axioma.estructuras{

	public class Lista{
		
		private var nodo:Nodo;
		private var numeroElementos:int;
		private var actual:Nodo;
		public static var SIGUIENTES:int = 0;
		public static var ANTERIORES:int = 1;
		public static var TODOS:int = 2;

		public function Lista()
		{
			nodo = null;
			numeroElementos=0;
		}
		
		public function add(valor:Object):Boolean
		{
			if(this.nodo==null)
			{
				this.nodo = new Nodo(valor);
			}
			else{
				var temp:Nodo = this.nodo;
				while(temp.getSiguiente()!=null)
				{
					temp = temp.getSiguiente();
				}
				var nNodo:Nodo=new Nodo(valor);
				nNodo.setAnterior(temp);
				temp.setSiguiente(nNodo);
			}
			this.numeroElementos++;
			return true;
		}
		
		public function get(indice:int):Object
		{
			if(this.nodo==null)
			{
				trace("Lista error");
				return null;
			}
			else{
				if(indice>=numeroElementos)
				{
					trace("Indice fuera del rango");
					return null;
				}
				else
				{
					var temp:Nodo = this.nodo;
					var i:int = 0;
					while(temp.getSiguiente()!=null && i<indice)
					{
						temp = temp.getSiguiente();
						i++;
					}
					return temp.getValor();
				}
			}
		}
		public function size():int
		{
			return this.numeroElementos;
		}
		public function getXActual():int
		{
			return this.actual.getValor().getX();
		}
		public function getYActual():int
		{
			return this.actual.getValor().getY();
		}
		public function getPermiteAsignarActual():Boolean
		{
			return this.actual.getValor().getPermiteAsignar();
		}
		public function getPermiteReservarActual():Boolean
		{
			return this.actual.getValor().getPermiteReservar();
		}
		public function getPermiteCupoExtraActual():Boolean
		{
			return this.actual.getValor().getPermiteCupoExtra();
		}

		public function ubicarEn(valor:Object):void
		{
			if(nodo==null)
			{
				trace("Error ubicando el elemento, lista vacia");
			}
			else{
				var temp:Nodo = this.nodo;
				while(temp.getSiguiente()!=null && temp.getValor()!=valor)
				{
					temp = temp.getSiguiente();
				}
				actual=temp;
			}
		}
		
		public function seleccionarHastaCoordenada(y:int):void
		{
			var objeto:Nodo =this.actual;
			if(y>this.getYActual())
			{
				while(objeto!=null)
				{
					if(objeto.getValor().getY()<y)
					{
						if(objeto.getValor().getHabilitado()){
							objeto.getValor().setSeleccionado(true);
						}
					}
					else{
						objeto.getValor().setSeleccionado(false);
					}
					objeto=objeto.getSiguiente();
				}
				deseleccionar(Lista.ANTERIORES);
			}
			else
			{
				while(objeto!=null)
				{
					if(objeto.getValor().getY()+objeto.getValor().height>y)
					{
						objeto.getValor().setSeleccionado(true);
					}
					else{
						objeto.getValor().setSeleccionado(false);
					}
					objeto=objeto.getAnterior();
				}
				deseleccionar(Lista.SIGUIENTES);
			}
		}
		public function deseleccionar(direccion:int)
		{
			var objeto:Nodo ;
			if(actual!=null)
			{
				switch(direccion)
				{
					case Lista.ANTERIORES:
						objeto=this.actual.getAnterior();
						while(objeto!= null)
						{
							objeto.getValor().setSeleccionado(false);
							objeto=objeto.getAnterior();
						}
					break;
					case Lista.SIGUIENTES:
						objeto=this.actual.getSiguiente();
						while(objeto!= null)
						{
							objeto.getValor().setSeleccionado(false);
							objeto=objeto.getSiguiente();
						}
					break;
					case Lista.TODOS:
						objeto=this.nodo;
						while(objeto!=null)
						{
							objeto.getValor().setSeleccionado(false);
							objeto=objeto.getSiguiente();
						}
					break;
					default:
						trace("Error, Por favor utilice las constantes de la clase Lista");
				}
			}
		}
		
		public function getHoraInicioSeleccion():String
		{
			var temp:Nodo = this.nodo;
			while(temp.getSiguiente()!=null && !temp.getValor().getSeleccionado())
			{
				temp = temp.getSiguiente();
			}
			var horaInicioSeleccion:String = temp.getValor().getHora();
			while(temp.getSiguiente()!=null && temp.getSiguiente().getValor().getSeleccionado())
			{
				temp = temp.getSiguiente();
			}
			var horaFinSeleccion:String = temp.getValor().getHoraFin();
			return horaInicioSeleccion;
		}
		public function getHoraFinSeleccion():String
		{
			var temp:Nodo = this.actual;
			while(temp.getSiguiente()!=null && !temp.getValor().getSeleccionado())
			{
				temp = temp.getSiguiente();
			}
			while(temp.getSiguiente()!=null && temp.getSiguiente().getValor().getSeleccionado())
			{
				temp = temp.getSiguiente();
			}
			var horaFinSeleccion:String = temp.getValor().getHoraFin();
			return horaFinSeleccion;
		}
		
		public function getCodigoAgendaSeleccionado():int
		{
			return actual.getValor().getCodigoAgenda();
		}
		public function imprimir():void
		{
			var temp:Nodo = this.nodo;
			while(temp!=null)
			{
				if(temp.getAnterior()!=null)
				{
					trace("Ant: "+temp.getAnterior().getValor());
				}
				else
				{
					trace("null");
				}
				trace(temp.getValor());
				if(temp.getSiguiente()!=null)
				{
					trace("Sig: "+temp.getSiguiente().getValor());
				}
				else
				{
					trace("null");
				}
				trace("");
				temp=temp.getSiguiente();
			}

		}
		public function getActual():Object
		{
			return actual.getValor();
		}
	}
	
	
}