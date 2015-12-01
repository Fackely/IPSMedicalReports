package mundo.agendaOdontologica {
	
	import flash.display.Sprite;
	import flash.text.TextField;
    import flash.text.TextFieldAutoSize;
	import flash.text.TextFormat;
	import flash.display.LineScaleMode;
	import flash.display.CapsStyle;
	import flash.display.JointStyle;
	import flash.text.AntiAliasType;
	
	public class HorarioOdo extends Sprite{
		
		// Constants:
		// Public Properties:		
		// Private Properties:
		private var codigo:String;
		private var labelhora:TextField;
	
		// Initialization:
		public function HorarioOdo(							   
								   posx:int,
								   posy:int,
								   hora:int,
								   min:int,
								   formathora:TextFormat,
								   colorIn:uint,
								   largoIntervalo:int) 
		{ 
			this.setCodigo = hora+min+"";
			this.graphics.beginFill(colorIn);
			this.graphics.lineStyle(2,colorIn,1,true,LineScaleMode.NONE,CapsStyle.ROUND,JointStyle.ROUND);
			this.graphics.drawRect(0,0,50,largoIntervalo);
			this.alpha = 0.5;
			this.graphics.endFill();
			
			labelhora = new TextField();
			labelhora.defaultTextFormat = formathora;
			labelhora.autoSize = TextFieldAutoSize.CENTER;
			labelhora.selectable = false;
			labelhora.antiAliasType = AntiAliasType.NORMAL;
			labelhora.text = doubleDigitFormat(hora)+":"+doubleDigitFormat(min);
			labelhora.x = (this.width/2)  - (labelhora.width/2);
			labelhora.y = ((this.height/2) - (labelhora.height/2)) - 2;
			this.addChild(labelhora);
			
			this.x = posx;
			this.y = posy;
			this.height = largoIntervalo;
		}
	
		// Public Methods:
		// Protected Methods:
		
		
		public function set setCodigo(valor:String)
		{
			this.codigo = valor;
		}
		
		public function get getCodigo():String
		{
			return this.codigo;
		}
		
		public function doubleDigitFormat(num:Number):String 
		{
	    	if(num < 10)
		        return ("0" + num);
			
		    return num+"";
		}
	}	
}