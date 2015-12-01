package mundo.indicePlaca
{
	import flash.display.MovieClip;
	import flash.events.MouseEvent;
	import fl.controls.CheckBox;
	import util.general.Constantes;	
	
	public class Diente extends MovieClip 
	{
		//Numero del diente
		var numeroDiente:int;
		
		//Indica si el diente esta activo
		var activo:String;
		
		//Checkbox del movie clip
		var ausente:CheckBox;
		
		//Indica si el diente esta siendo excluido por otro diente		
		var excluido:String;
		
		//Numero del diente con el cual se excluye
		var dienteExcluido:int;
		
		//Superficie Vestibular
		var vestibular:SuperficieDiente;
		
		//Superficie distal
		var distal:SuperficieDiente;
		
		//Superficie mesial
		var mesial:SuperficieDiente;
		
		//Superficie lingual
		var lingual:SuperficieDiente;
		
		/**
		
		*/
		public function Diente(param_diente:int,param_activo:String,param_excluido:String)
		{
			ausente = new CheckBox();
			
			numeroDiente = param_diente;
			activo = param_activo;
			excluido = param_excluido;
			dienteExcluido = getNumeroDienteExcluye(param_diente);
			
			//Define la seccion vestibular
			vestibular = new SuperficieDiente("vestibular",Constantes.acronimoNo);
			vestibular.x = -0.4;
			vestibular.y = -28.5;
			this.addChildAt(vestibular,0);
			
			//Define la seccion distal
			distal = new SuperficieDiente("distal",Constantes.acronimoNo);
			distal.x = -30;
			distal.y = 1;
			distal.rotation = -90;
			this.addChildAt(distal,0);
			
			//Define la seccion mesial
			mesial = new SuperficieDiente("mesial",Constantes.acronimoNo);
			mesial.x = 28.9
			mesial.y = 1.1;
			mesial.rotation = 90;
			this.addChildAt(mesial,0);
			
			//Define la seccion lingual
			lingual = new SuperficieDiente("lingual",Constantes.acronimoNo);
			lingual.x = 0.5;
			lingual.y = 29.5;
			lingual.rotation = 180;
			this.addChildAt(lingual,0);		
						
			this.gotoAndStop(1);			
			this.useHandCursor = true;
			
			if(activo == Constantes.acronimoSi)
			{
				this.addEventListener(MouseEvent.MOUSE_OVER,sumTamano);			
				this.addEventListener(MouseEvent.MOUSE_OUT,restTamano);			
				this.ausente.addEventListener(MouseEvent.CLICK,evaluarAusente);
			}
		}
		
		/**
		 * Función encargada de evaluar el porcentaje cuando el diente está ausente
		 * @paramn e Evento del mouse
		 */
		public function evaluarAusente(e:MouseEvent):void
		{
			var placa:Object = this.parent.parent as Object;	
			var cont:int = 0;
			var parentObjP:Object = this.parent as Object;	
			
			if(ausente.selected == true)
			{
				if(this.vestibular.getMarcado == Constantes.acronimoSi)
				{
					this.vestibular.pintarBlanco();
					cont++;
				}
				
				if(this.vestibular.getMarcado == Constantes.acronimoSi)
				{
					this.vestibular.pintarBlanco();
					cont++;
				}
				
				if(this.distal.getMarcado == Constantes.acronimoSi)
				{
					this.distal.pintarBlanco();
					cont++;
				}
				
				if(this.mesial.getMarcado == Constantes.acronimoSi)
				{
					this.mesial.pintarBlanco();
					cont++;
				}
				
				if(this.lingual.getMarcado == Constantes.acronimoSi)
				{			
					this.lingual.pintarBlanco();
					cont++;
				}
				
				if(cont>0)				
				{
					placa.operNumSuperDentSelec(cont,"rest");
				}
								
				//incluye el diente
				this.incluirDiente();
				
				//Excluye el diente asociado
				if(this.getDienteExcluido > 0)
				{
					var numDienteExclu:int = placa.getPosNumeroDiente(this.getDienteExcluido);
					
					if(numDienteExclu > 0)
					{								
						parentObjP.getChildAt(numDienteExclu).excluirDiente();
					}
				}				
				
				if(this.excluido == Constantes.acronimoNo)				
				{
					placa.operNumSuperDentPrese(4,"rest");
				}
			}
			else
			{
				//incluye el diente
				this.incluirDiente();
				
				//if(this.excluido == Constantes.acronimoSi)				
				{
					placa.operNumSuperDentPrese(4,"sum");
				}
			}
		}
		
		/**
		excluye un diente
		*/
		public function incluirDiente():void
		{			
			var cont:int = 0;
			var placa:Object = this.parent.parent as Object;						

			if(this.activo == Constantes.acronimoSi && 
			   this.excluido == Constantes.acronimoSi)
			{
					placa.operNumSuperDentPrese(4,"sum");
					this.excluido = Constantes.acronimoNo;
					trace("se libero el diente >> "+this.numeroDiente);			
			}
		}
		
		/**
		excluye un diente
		*/
		public function excluirDiente():void
		{
			var cont:int = 0;
			var placa:Object = this.parent.parent as Object;						

			if(this.activo == Constantes.acronimoSi && 
		   		this.excluido == Constantes.acronimoNo)			
			{
				if(this.ausente.selected == false)
				{
					if(this.vestibular.getMarcado == Constantes.acronimoSi)
					{
						this.vestibular.pintarBlanco();
						cont++;
					}
					
					if(this.vestibular.getMarcado == Constantes.acronimoSi)
					{
						this.vestibular.pintarBlanco();
						cont++;
					}
					
					if(this.distal.getMarcado == Constantes.acronimoSi)
					{
						this.distal.pintarBlanco();
						cont++;
					}
					
					if(this.mesial.getMarcado == Constantes.acronimoSi)
					{
						this.mesial.pintarBlanco();
						cont++;
					}
					
					if(this.lingual.getMarcado == Constantes.acronimoSi)
					{
						this.lingual.pintarBlanco();
						cont++;
					}
					
					if(cont>0)
						placa.operNumSuperDentSelec(cont,"rest");				

					this.excluido = Constantes.acronimoSi;				
					placa.operNumSuperDentPrese(4,"rest");
					trace("se excluyo el diente >> "+this.numeroDiente);
				}
				else
				{
					this.ausente.selected = false;
					this.excluido = Constantes.acronimoSi;
					trace("se excluyo el diente >> "+this.numeroDiente);
				}
			}
		}
		
		/**
		Devuelve el numero del diente con el cual se excluye
		*/
		public function getNumeroDienteExcluye(param_diente:int):int
		{
			//primer cuadrante
			
			if(param_diente == 15)			
				return 55;			
			else if(param_diente == 14)
				return 54;			
			else if(param_diente == 13)
				return 53;
			else if(param_diente == 12)
				return 52;
			else if(param_diente == 11)
				return 51;
			else if(param_diente == 55)
				return 15;
			else if(param_diente == 54)
				return 14;			
			else if(param_diente == 53)
				return 13;
			else if(param_diente == 52)
				return 12;
			else if(param_diente == 51)
				return 11;
				
			//Segundo Cuadrante	
				
			if(param_diente == 21)			
				return 61;
			else if(param_diente == 22)
				return 62;
			else if(param_diente == 23)
				return 63;
			else if(param_diente == 24)
				return 64;
			else if(param_diente == 25)
				return 65;
			else if(param_diente == 61)
				return 21;
			else if(param_diente == 62)
				return 22;
			else if(param_diente == 63)
				return 23;
			else if(param_diente == 64)
				return 24;
			else if(param_diente == 65)
				return 25;
				
			//tercer cuadrante	
				
			if(param_diente == 45)
				return 85;
			else if(param_diente == 44)
				return 84;
			else if(param_diente == 43)
				return 83;
			else if(param_diente == 42)
				return 82;
			else if(param_diente == 41)
				return 81;
			else if(param_diente == 85)
				return 45;
			else if(param_diente == 84)
				return 44;
			else if(param_diente == 83)
				return 43;
			else if(param_diente == 82)
				return 42;
			else if(param_diente == 81)
				return 41;
				
			//Cuarto cuadrante	
				
			if(param_diente == 71)
				return 31;
			else if(param_diente == 72)
				return 32;
			else if(param_diente == 73)
				return 33;
			else if(param_diente == 74)
				return 34;
			else if(param_diente == 75)
				return 35;
			else if(param_diente == 31)
				return 71;
			else if(param_diente == 32)
				return 72;
			else if(param_diente == 33)
				return 73;
			else if(param_diente == 34)
				return 74;
			else if(param_diente == 35)
				return 75;
		
			return Constantes.codigoNuncaValido;
		}
		
		/**
		
		*/
		public function esDienteUsado():Boolean
		{
			if(vestibular.getMarcado == Constantes.acronimoSi || 
			   	distal.getMarcado == Constantes.acronimoSi || 
					mesial.getMarcado == Constantes.acronimoSi || 
						lingual.getMarcado == Constantes.acronimoSi)
				return true;
				
			return false;	
		}
				
		/**
		
		*/		
		public function sumTamano(event:MouseEvent):void
		{			
			if(ausente.selected == false)
			{			
				this.height = 59;
				this.width = 59;
				this.gotoAndStop(2);
			}
		}
		
		public function restTamano(event:MouseEvent):void
		{
			if(ausente.selected == false)
			{	
				this.height = 50;
				this.width = 50;
				this.gotoAndStop(1);
			}
		}
		
		public function get getAusente():CheckBox
		{
			return this.ausente;
		}
		
		public function set setAusente(param_value:CheckBox):void
		{
			this.ausente = param_value;
			this.ausente.addEventListener(MouseEvent.CLICK,evaluarAusente);
		}
		
		public function get getVestibular():SuperficieDiente
		{
			return this.vestibular;
		}
		
		public function get getDistal():SuperficieDiente
		{
			return this.distal;
		}
		
		public function get getMesial():SuperficieDiente
		{
			return this.mesial;
		}
		
		public function get getLingual():SuperficieDiente
		{
			return this.lingual;
		}
		
		public function get getNumeroDiente():int
		{
			return this.numeroDiente;
		}
		
		public function set setNumeroDiente(param_value:int):void
		{
			this.numeroDiente = param_value;
		}
		
		public function get getDienteExcluido():int
		{
			return this.dienteExcluido;
		}
		
		public function set setDienteExcluido(param_value:int):void
		{
			this.dienteExcluido = param_value;
		}		
		
		public function get getExcluido():String
		{
			return this.excluido;
		}
		
		public function set setExcluido(param_value:String):void
		{
			this.excluido = param_value;
		}
		
		public function get getActivo():String
		{
			return this.activo;
		}
		
		public function set setActivo(param_value:String):void
		{
			this.activo = param_value;
		}
	}
}