package mundo.agendaOdontologica 
{
	import flash.display.Sprite;
	import flash.display.LineScaleMode;
	import flash.display.CapsStyle;
	import flash.display.JointStyle;
	import flash.text.TextField;
    import flash.text.TextFieldAutoSize;
	import flash.text.TextFormat;
	import flash.external.ExternalInterface;
	import flash.events.MouseEvent;
	
	public class SubAgenda extends Sprite {
		
		// Constants:
		// Public Properties:
		// Private Properties:
		private var horaInicio:int = 0;
		private var minInicio:int = 0;
		private var horaFinal:int = 0;
		private var minFinal:int = 0;
		private var numCitas:int = 0;
		private var citasArray:Array;
		private var format:TextFormat;
		var labelAge:TextField = new TextField();		
	
		// Initialization:
		public function SubAgenda(horaInicioP:int,minInicioP:int,horaFinalP:int,minFinalP:int) 
		{ 
			this.horaInicio = horaInicioP;
			this.minInicio = minInicioP;
			this.horaFinal = horaFinalP;
			this.minFinal = minFinalP
			this.citasArray = new Array();
			
			format = new TextFormat();
			format.color = 0xFFFFFF;
			format.size = 11;
			format.bold = false;
			format.italic = false;
			
		}
		
		// Public Methods:
		
		/**
		
		*/
		public function pintarSubAgenda(anchoSubAgenda:int,posx:int,posy:int)
		{
			this.graphics.beginFill(0x0145A9);
			this.graphics.lineStyle(2,0xDDEEFF,1,true,LineScaleMode.NONE,CapsStyle.ROUND,JointStyle.BEVEL);
			this.graphics.drawRect(0,0,anchoSubAgenda,20);
			this.graphics.endFill();
			this.alpha = 0.9;
			
			this.graphics.beginFill(0x0145A9);
			this.graphics.lineStyle(2,0xDDEEFF,1,true,LineScaleMode.NONE,CapsStyle.ROUND,JointStyle.BEVEL);
			this.graphics.drawRect(0,0,8,8);
			this.graphics.endFill();
			
			this.buttonMode = true;
			this.useHandCursor =true;
						
			labelAge.defaultTextFormat = format;
			labelAge.selectable = false;
			labelAge.mouseEnabled = false;
			labelAge.text = doubleDigitFormat(horaInicio)+":"+doubleDigitFormat(minInicio)+" - "+doubleDigitFormat(horaFinal)+":"+doubleDigitFormat(minFinal);
			labelAge.autoSize = TextFieldAutoSize.CENTER;
			labelAge.x = 36;
			labelAge.y = 1;
			
			this.addChild(labelAge);
			
			this.x = posx;
			this.y = posy;
			this.width = anchoSubAgenda;
			
			if(this.citasArray.length == 1)			
				pintarCitas(anchoSubAgenda,true);
			else	
				pintarCitas(anchoSubAgenda,false);
				
			this.addEventListener(MouseEvent.CLICK,selecciono);
			this.addEventListener(MouseEvent.MOUSE_OVER,mostrarInfo);
			this.addEventListener(MouseEvent.MOUSE_OUT,ocultarInfo);			
		}
		
		/**
		*/
		private function selecciono(e:MouseEvent)
		{
			if(e.target is SubAgenda)
			{
				var agenda:Object = this.parent as Object;
				trace("llamadoSubAgenda "+agenda.getCodigoPk+" "+agenda.getIndicador+" "+doubleDigitFormat(horaInicio)+" "+doubleDigitFormat(minInicio));
				String(ExternalInterface.call("llamadoSubAgenda",
											  agenda.getCodigoPk,
											  agenda.getIndicador,
											  doubleDigitFormat(horaInicio),
											  doubleDigitFormat(minInicio)));
			}
		}
		
		/**
		
		*/
		private function mostrarInfo(e:MouseEvent)
		{
			if(e.target is SubAgenda)
			{
				var agenda:Object = this.parent.parent.parent;
				var agendaOdo:Object = this.parent as Object;
				agenda.mostrarInfoComponente("agenda",agendaOdo.cargarInfo(),(agendaOdo.x+agendaOdo.width+10),e.target.parent.parent.mouseY);
				
				labelAge.textColor = 0x000000;
				labelAge.background = true;
				labelAge.backgroundColor =  0xC4DCFF;
			}
		}
		
		/**
		
		*/
		private function ocultarInfo(e:MouseEvent)
		{			
			if(e.target is SubAgenda)
			{
				var agenda:Object = this.parent.parent.parent;
				agenda.ocultarInfo("agenda");
				labelAge.background = false;
				labelAge.textColor = 0xFFFFFF;
			}
		}
		
		
		/**
		
		*/
		public function pintarCitas(anchoCita:int,isVerTodaInfo:Boolean)
		{
			var num:int = citasArray.length;
			var posy:int = 25;
			for(var i = 0; i < num; i++)
			{				
				citasArray[i].pintarCita(anchoCita,0,posy,isVerTodaInfo);
				posy = posy + citasArray[i].height + 1;
				this.addChild(citasArray[i]);
			}
		}
	
		/**
		*/
		public function doubleDigitFormat(num:Number):String 
		{
	    	if(num < 10)
		        return ("0" + num);
			
		    return num+"";
		}
		
		/**
		
		*/
		public function set setCitasArray(valor:Array)
		{
			this.citasArray = valor;
		}
		
		/**
		*/
		public function get getCitasArray():Array
		{
			return this.citasArray;
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

				
		// Protected Methods:
	}
	
}