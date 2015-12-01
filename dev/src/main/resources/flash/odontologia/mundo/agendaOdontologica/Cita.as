package mundo.agendaOdontologica {
	
	import flash.display.Sprite;
	import flash.display.LineScaleMode;
	import flash.display.CapsStyle;
	import flash.display.JointStyle;
	import flash.text.TextField;
	import flash.text.TextFieldType; 
    import flash.text.TextFieldAutoSize;
	import flash.text.TextFormat;
	import flash.events.MouseEvent;
	import flash.external.ExternalInterface;
	
	public class Cita extends Sprite{
		
		// Constants:
		// Public Properties:
		// Private Properties:
		private var codigoPk:String;
		private var horaInicio:int;
		private var minInicio:int;
		private var horaFinal:int;
		private var minFinal:int;
		private var descPaciente:String;
		private var tipoIdenPac:String;
		private var numIdenPac:String;
		private var observaciones:String;
		private var descProfesional:String;
		private var estado:String;
		private var duracion:String;
		private var format:TextFormat;
		private var esUnica:Boolean;
		private var anchoOriginal:int;		
		private var posCita:int;		
		private var labelCita:TextField = new TextField();
		
		// Initialization:
		public function Cita(
							 codigoPkParam:String,
							 horaInicioParam:int,
							 minInicioParam:int,
							 horaFinalParam:int,
							 minFinalParam:int,
							 descPacienteParam:String,
							 tipoIdenPacParam:String,
							 numIdenPacParam:String,
							 observacionesParam:String,
							 estadoParam:String,
							 descProfesionalParam:String,
							 duracionParam:String,
							 posCitaParam:int
							 )
		{
			codigoPk = codigoPkParam;
			horaInicio = horaInicioParam;
			minInicio = minInicioParam;
			horaFinal = horaFinalParam;
			minFinal = minFinalParam;
			descPaciente = descPacienteParam.toLowerCase();			
			tipoIdenPac = tipoIdenPacParam;
			numIdenPac = numIdenPacParam;
			observaciones = observacionesParam;
			estado = estadoParam;
			descProfesional = descProfesionalParam;	
			duracion = duracionParam;
			posCita = posCitaParam;
			
			format = new TextFormat();
			format.color = 0x000000;
			format.size = 11;
			format.bold = true;
			format.italic = false;
			
			this.buttonMode = true;
			this.useHandCursor =true;
		}
		
		// Public Methods:
		
		/**
		
		*/
		public function pintarCita(anchoCita:int,posx:int,posy:int,isVerTodaInfo:Boolean)
		{
			this.graphics.beginFill(0xC4DCFF);
			this.graphics.lineStyle(1,0x0145A9,1,true,LineScaleMode.NONE,CapsStyle.ROUND,JointStyle.BEVEL);
			esUnica = isVerTodaInfo;
			
			if(isVerTodaInfo)
				this.graphics.drawRect(0,0,anchoCita,60);
			else	
				this.graphics.drawRect(0,0,anchoCita,20);
			
			this.graphics.beginFill(0x0145A9);
			this.graphics.lineStyle(1,0xCCCCCC,1,true,LineScaleMode.NONE,CapsStyle.ROUND,JointStyle.BEVEL);
			
			this.graphics.endFill();
			
			//var labelCita:TextField = new TextField();
			labelCita.defaultTextFormat = format;
			labelCita.selectable = false;
			
			var contenido:String = "";			
			if(isVerTodaInfo)
				contenido = estado+"\n"+this.descPaciente.substring(0,26)+"\n"+(this.tipoIdenPac+" "+numIdenPac).substring(0,26)+"\nProf. "+descProfesional.substring(0,26);
			else
				contenido =  descPaciente.substring(0,26);
			
			labelCita.text = contenido;
			labelCita.type = TextFieldType.DYNAMIC;
			labelCita.mouseEnabled = false;
			labelCita.autoSize = TextFieldAutoSize.CENTER;
			labelCita.x = 3;
			labelCita.y = 0;
			
			this.addChild(labelCita);
			
			this.x = posx;
			this.y = posy;
			this.width = anchoCita;
			this.anchoOriginal = anchoCita;
			
			this.addEventListener(MouseEvent.MOUSE_OVER,mostrarInfo);
			this.addEventListener(MouseEvent.MOUSE_OUT,ocultarInfo);
			this.addEventListener(MouseEvent.CLICK,seleccionoCita);
		}
		
		/**
		*/
		private function seleccionoCita(e:MouseEvent)
		{
			if(e.target is Cita)
			{
				var agendaOdo:Object = this.parent.parent as Object;
				
				trace("llamadoCitaPaciente "+codigoPk+" "+posCita+" "+agendaOdo.getCodigoPk+" "+agendaOdo.getIndicador);				
				String(ExternalInterface.call("llamadoCitaPaciente",agendaOdo.getCodigoPk,agendaOdo.getIndicador,codigoPk,posCita));
			}
		}
		
		/**		
		*/
		private function mostrarInfo(e:MouseEvent)
		{						
			if(e.target is Cita)
			{
				var agendaOdo:Object = this.parent.parent as Object;
				var agenda:Object = this.parent.parent.parent.parent as Object;
				
				var info:Array = new Array("","","","","");
				info[0] = "Información Cita";
				info[1] = ""+estado+"                 "+duracion+" min";
				info[2] = ""+this.descPaciente.substring(0,26);				
				info[3] = ""+(this.tipoIdenPac+" "+numIdenPac).substring(0,26);
				info[4] = "Prof. "+agendaOdo.getDesProfesional.substring(0,26);
				info[5] = "Hora Inicio: "+doubleDigitFormat(horaInicio)+" : "+doubleDigitFormat(minInicio);
				info[6] = "Hora Fin: "+doubleDigitFormat(horaFinal)+" : "+doubleDigitFormat(minFinal);
						
				agenda.mostrarInfoComponente("cita",info,(this.parent.parent.x+this.parent.parent.width+10),e.target.parent.parent.parent.mouseY);			
				agenda.mostrarInfoComponente("agenda",agendaOdo.cargarInfo(),(this.parent.parent.x+this.parent.parent.width+10),e.target.parent.parent.parent.mouseY+140);
				this.x = this.x + 5;

				labelCita.textColor = 0x000000;
				labelCita.background = true;
				labelCita.backgroundColor =  0xFFF4CC;
			}
		}
		
		/**
		
		*/
		private function ocultarInfo(e:MouseEvent)
		{
			if(e.target is Cita)
			{
				var agenda:Object = this.parent.parent.parent.parent as Object;			
				agenda.ocultarInfo("cita");
				agenda.ocultarInfo("agenda");
				this.x = this.x - 5;
				labelCita.background = false;
			}
		}
	
		public function doubleDigitFormat(num:Number):String 
		{
	    	if(num < 10)
		        return ("0" + num);
			
		    return num+"";
		}
		// Protected Methods:
	}
	
}