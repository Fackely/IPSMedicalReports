package mundo.agendaOdontologica 
{	
	import flash.display.Sprite;
	import flash.text.TextField;
    import flash.text.TextFieldAutoSize;
	import flash.text.TextFormat;
	import flash.display.LineScaleMode;
	import flash.display.CapsStyle;
	import flash.display.JointStyle;
	import flash.text.AntiAliasType;
	import flash.events.MouseEvent;
	import flash.external.ExternalInterface;
	
	public class AgendaOdo extends Sprite {
		
		// Constants:
		// Public Properties:
		// Private Properties:
		private var codigoPk:String;
		private var colorAge:String;
		private var indicador:String;
		private var horaInicio:int;
		private var minInicio:int;
		private var horaFinal:int;
		private var minFinal:int;
		private var codConsultorio:String;
		private var desConsultorio:String;
		private var posConsultorio:int;
		private var desProfesional:String;
		private var desEspecialidad:String;
		private var numCitas:int;
		private var subAgendasArray:Array;
	
		// Initialization:
		public function AgendaOdo(
								  codigoPkParam:String,
								  posx:int,
								  posy:int,
								  formatAgenda:TextFormat,
								  colorIn:uint,								  
								  largoAgenda:int,
								  anchoAgenda:int,
								  numCitasParam:int) 
		{
			this.name = codigoPkParam;
			this.codigoPk = codigoPkParam;
			
			this.graphics.beginFill(colorIn);
			this.graphics.lineStyle(1,0x011F4B,1,true,LineScaleMode.NONE,CapsStyle.ROUND,JointStyle.BEVEL);
			this.graphics.drawRect(0,0,anchoAgenda,largoAgenda);			
			this.graphics.endFill();
			
			this.x = posx;
			this.y = posy;
			this.width = anchoAgenda;
			this.height = largoAgenda;			
			
			this.addEventListener(MouseEvent.MOUSE_OVER,mostrarInfo);
			this.addEventListener(MouseEvent.MOUSE_OUT,ocultarInfo);
			
			ayuda(formatAgenda);
			
			this.width = anchoAgenda;
			this.height = largoAgenda;
			this.numCitas = numCitasParam;
			
			this.subAgendasArray = new Array();
		}
		
		// Protected Methods:
		public function ayuda(formatAgenda:TextFormat)
		{
			var sp_ayuda:Sprite = new Sprite();
			sp_ayuda.graphics.beginFill(0xF7FEFF);
			sp_ayuda.graphics.lineStyle(3,0x0145A9,0.5);
			sp_ayuda.graphics.drawRect(0,0,20,20);
			sp_ayuda.graphics.endFill();			
			
			var labelAge:TextField = new TextField();
			labelAge.defaultTextFormat = formatAgenda;
			labelAge.selectable = false;
			labelAge.mouseEnabled = false;
			labelAge.text = "+";
			labelAge.autoSize = TextFieldAutoSize.CENTER;
			labelAge.antiAliasType = AntiAliasType.NORMAL; 
			labelAge.border = false;
			labelAge.x = 4;
			labelAge.y = -2;
			
			sp_ayuda.x = this.width-20;
			sp_ayuda.y = 0;
			
			sp_ayuda.buttonMode = true;
			sp_ayuda.useHandCursor =true;

			sp_ayuda.addChild(labelAge);			
			
			sp_ayuda.addEventListener(MouseEvent.CLICK,selecciono);
			this.addChild(sp_ayuda);
		}
		
		/**
		
		*/
		private function mostrarInfo(e:MouseEvent)
		{			
			if(e.target is AgendaOdo)
			{
				var agenda:Object = this.parent.parent as Object;
				//se apunta al sprite
				agenda.mostrarInfoComponente("agenda",cargarInfo(),(this.x+this.width+10),e.target.parent.mouseY);			
			}
		}
		
		/**
		*/
		private function selecciono(e:MouseEvent)
		{
			trace("llamadoAgenda "+codigoPk+" "+indicador);
			String(ExternalInterface.call("llamadoAgenda",codigoPk,indicador));
		}
		
		/**
		
		*/
		public function cargarInfo():Array
		{
		 	var info:Array = new Array("","","","","");
			info[0] = "Información Agenda";
			
			if(this.desProfesional+"" != "")
			{
				info[1] = "Prof.: "+this.desProfesional.substring(0,26);				
				info[2] = "Espe.: "+this.desEspecialidad.substring(0,26);
			}
			else
			{
				info[1] = "Prof.: SIN PROFESIONAL";
				info[2] = "Espe.:";
			}
			
			info[3] = "Hora Inicio: "+doubleDigitFormat(horaInicio)+" : "+doubleDigitFormat(minInicio);
			info[4] = "Hora Fin: "+doubleDigitFormat(horaFinal)+" : "+doubleDigitFormat(minFinal);
			
			return info;
		}
		
		/**
		
		*/
		public function pintarSubConsultas(
										   largoIntervalo:int,
										   espacioEntreElementos:int,
										   posyIniciaRango:int,
										   intervalo:int)
		{
			var numSubAge:int = subAgendasArray.length;
			var posy:int = 0;
			for(var i = 0; i < numSubAge; i++)
			{
				posy = getPosXSubAgenda(
										horaInicio,
										minInicio,
										subAgendasArray[i].getHoraInicio,
										subAgendasArray[i].getMinInicio,
										largoIntervalo,
										espacioEntreElementos,
										posyIniciaRango,
										intervalo);
				
				subAgendasArray[i].pintarSubAgenda(this.width-2,2,posy);
				posy = posy + subAgendasArray[i].height + 5;
				this.addChild(subAgendasArray[i]);
			}
		}
		
		/**		
		Calcula la posición Y para la ubicación de las SubAgendas
		*/
		public function getPosXSubAgenda(
										 horaInicioAge:int,
										 minutoInicioAge:int,
										 horaInicioSub:int,
										 minutoInicioSub:int,
										 largoIntervalo:int,
										 espacioEntreElementos:int,
										 posyIniciaRango:int,
										 intervalo:int):int
		{			
			var posy:int = 0;
			var hora:Date = new Date();
			var horaInicio:Date = new Date();
			var numInterAnte:int = 0;
			
			hora.setHours(Number(horaInicioAge),Number(minutoInicioAge));
				
			horaInicio.setHours(Number(horaInicioSub),
								Number(minutoInicioSub));
			
			//captura el numero de intervalos anteriores a la agenda
			numInterAnte = (((horaInicio.getTime() - hora.getTime()) / 1000) / 60) / intervalo;

			if(horaInicioAge == horaInicioSub && horaInicioSub == minutoInicioSub)
				posy = 0;
			else
				posy = ((largoIntervalo + espacioEntreElementos) * numInterAnte) ;
				
			return posy;	
		}		
		
		/**
		
		*/
		private function ocultarInfo(e:MouseEvent)
		{			
			if(e.target is AgendaOdo)
			{
				var agenda:Object = this.parent.parent as Object;
				agenda.ocultarInfo("agenda");
			}
		}
		
	
		// Public Methods:
		
		/**
		*/
		public function get getCodigoPk():String
		{
			return this.codigoPk;
		}
		
		/**
		*/
		public function get getColorAge():String
		{
			return this.colorAge;
		}
		
		/**
		*/
		public function get getHoraInicio():int
		{
			return this.horaInicio;
		}
		
		/**
		*/
		public function get getMinInicio():int
		{
			return this.minInicio;
		}
		
		/**
		*/
		public function get getHoraFinal():int
		{
			return this.horaFinal;
		}
		
		/**
		*/
		public function get getMinFinal():int
		{
			return this.minFinal;
		}
		
		/**
		*/
		public function get getCodConsultorio():String
		{
			return this.codConsultorio;
		}
		
		/**
		*/
		public function get getDesConsultorio():String
		{
			return this.desConsultorio;
		}
		
		/**
		*/
		public function get getPosConsultorio():int
		{
			return this.posConsultorio;
		}
		
		/**
		*/
		public function get getDesProfesional():String
		{
			return this.desProfesional;
		}
		
		/**
		*/
		public function get getDesEspecialidad():String
		{
			return this.desEspecialidad;
		}
		
		/**
		*/
		public function get getNunCitas():int
		{
			return this.numCitas;
		}
		
		/**
		*/
		public function get getSubAgendasArray():Array
		{
			return this.subAgendasArray;
		}
		
		/**
		*/
		public function get getIndicador():String
		{
			return this.indicador;
		}
		
		//**
		
		/**
		*/
		public function set setCodigoPk(valor:String)
		{
			this.codigoPk = valor;
		}
		
		/**
		*/
		public function set setColorAge(valor:String)
		{
			this.colorAge = valor;
		}
		
		/**
		*/
		public function set setHoraInicio(valor:int)
		{
			this.horaInicio = valor;
		}
		
		/**
		*/
		public function set setMinInicio(valor:int)
		{
			this.minInicio = valor;
		}
		
		/**
		*/
		public function set setHoraFinal(valor:int)
		{
			this.horaFinal = valor;
		}
		
		/**
		*/
		public function set setMinFinal(valor:int)
		{
			this.minFinal = valor;
		}
		
		/**
		*/
		public function set setCodConsultorio(valor:String)
		{
			this.codConsultorio = valor;
		}
		
		/**
		*/
		public function set setDesConsultorio(valor:String)
		{
			this.desConsultorio = valor;
		}
		
		/**
		*/
		public function set setPosConsultorio(valor:int)
		{
			this.posConsultorio = valor;
		}
		
		/**
		*/
		public function set setDesProfesional(valor:String)
		{
			this.desProfesional = valor;
		}
		
		/**
		*/
		public function set setDesEspecialidad(valor:String)
		{
			this.desEspecialidad = valor;
		}
		
		/**
		*/
		public function set setIndicador(valor:String)
		{
			this.indicador = valor;
		}
		
		/**
		
		*/
		public function set setSubAgendasArray(valor:Array)
		{
			this.subAgendasArray = valor;
		}
		
		/**
		*/
		public function set setNunCitas(valor:int)
		{
			this.numCitas = valor;
		}
		
		public function doubleDigitFormat(num:Number):String 
		{
	    	if(num < 10)
		        return ("0" + num);
			
		    return num+"";
		}
		
		
	}
	
}