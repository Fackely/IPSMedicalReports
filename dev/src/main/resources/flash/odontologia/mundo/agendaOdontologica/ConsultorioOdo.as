package mundo.agendaOdontologica {
	
	import flash.display.Sprite;
	import flash.text.TextField;
    import flash.text.TextFieldAutoSize;
	import flash.text.TextFormat;
	import flash.display.LineScaleMode;
	import flash.display.CapsStyle;
	import flash.display.JointStyle;
	import flash.text.AntiAliasType;
	
	public class ConsultorioOdo extends Sprite
	{		
		// Constants:
		// Public Properties:
		// Private Properties:
		private var nombre:String;
		private var codigo:String;
		private var labelConsul:TextField;
	
		// Initialization:
		public function ConsultorioOdo(
									   posx:int,
									   posy:int,
									   texto:String,
									   formatConsul:TextFormat,
									   colorIn:uint,
									   anchoConsultorio:int)
		{
			this.graphics.beginFill(colorIn);
			this.graphics.lineStyle(2,0xC4DCFF,1,true,LineScaleMode.NONE,CapsStyle.ROUND,JointStyle.ROUND);
			this.graphics.drawRect(0,0,anchoConsultorio,33);
			this.alpha = 0.7;
			
			labelConsul = new TextField();
			labelConsul.defaultTextFormat = formatConsul;
			
			var cadena:String = "";
			if(texto.length > 18)
				cadena = texto.substring(0,18)+"\n"+texto.substring(18,36);
			else
				cadena = texto;
			
			labelConsul.text = cadena;
			labelConsul.selectable = false;
			labelConsul.autoSize = TextFieldAutoSize.CENTER;
			labelConsul.antiAliasType = AntiAliasType.NORMAL; 
			labelConsul.x = (this.width/2)  - (labelConsul.width/2);
			labelConsul.y = ((this.height/2) - (labelConsul.height/2)) - 2;

			this.graphics.endFill();					
			this.addChild(labelConsul);
			
			this.x = posx;
			this.y = posy;
			this.width = anchoConsultorio;
			
			nombre = "";
			codigo = "";			

		}
	
		// Public Methods:
		
		public function set setNombre(valor:String)
		{
			this.nombre = valor;
		}
		
		public function get getNombre():String
		{
			return this.nombre;
		}
		
		public function set setCodigo(valor:String)
		{
			this.codigo = valor;
		}
		
		public function getCodigo():String
		{
			return this.codigo
		}
							
		
		// Protected Methods:
	}
	
}