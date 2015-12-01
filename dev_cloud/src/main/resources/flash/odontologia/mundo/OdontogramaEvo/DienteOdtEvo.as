package mundo.OdontogramaEvo
{
	import flash.display.MovieClip;
	import flash.events.MouseEvent;
	import flash.events.Event;
	import fl.controls.CheckBox;
	import flash.text.TextField;
	import flash.text.TextFormat;  
    import flash.text.TextFieldAutoSize; 
	import flash.text.AntiAliasType;
	import fl.controls.ComboBox;
	import fl.data.DataProvider;
	import fl.core.UIComponent;
	import fl.controls.ComboBox;
	import flash.display.Loader;
	import flash.net.URLRequest;
	import flash.display.Bitmap;
	import flash.display.BitmapData;
	import flash.display.Shape;
	import flash.text.StyleSheet;
	import flash.geom.Matrix;
	import flash.geom.Point;
	import flash.geom.Rectangle;


	import util.general.Constantes;	
	
	public class DienteOdtEvo extends MovieClip 
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
		var vestibular:SuperficieDienteOdtEvo;
		
		//Superficie distal
		var distal:SuperficieDienteOdtEvo;
		
		//Superficie mesial
		var mesial:SuperficieDienteOdtEvo;
		
		//Superficie lingual
		var lingual:SuperficieDienteOdtEvo;
		
		//Superficie Oclusal
		var oclusal:SuperficieDienteOdtEvo;
		
		//indica si el diente esta siendo editado
		var isEditado:Boolean;
		
		//Poscion x original
		private var posx:int;
		
		//Posicion y original
		private var posy:int;			
		
		//path Hallazgo diente
		private var pathImagen:String;
		
		//
		private var bitmap:Bitmap;
		
		//
		private var colorS:String;
		
		/**
		
		*/
		public function DienteOdtEvo(param_diente:int,param_activo:String,param_excluido:String)
		{
			this.bitmap = new Bitmap();
			this.name = param_diente+"";
			ausente = new CheckBox();			
			
			numeroDiente = param_diente;
			activo = param_activo;
			excluido = param_excluido;
			dienteExcluido = getNumeroDienteExcluye(param_diente);
			
			//Define la seccion vestibular
			vestibular = new SuperficieDienteOdtEvo("1","vestibular",Constantes.codigoSectorDiente1);
			vestibular.x = -0.4;
			vestibular.y = -28.5;
			this.addChildAt(vestibular,0);
			
			//Define la seccion distal
			distal = new SuperficieDienteOdtEvo("2","distal",Constantes.codigoSectorDiente4);
			distal.x = -30;
			distal.y = 1;
			distal.rotation = -90;
			this.addChildAt(distal,0);
			
			//Define la seccion mesial
			mesial = new SuperficieDienteOdtEvo("3","mesial",Constantes.codigoSectorDiente2);
			mesial.x = 28.9
			mesial.y = 1.1;
			mesial.rotation = 90;
			this.addChildAt(mesial,0);
			
			//Define la seccion lingual
			lingual = new SuperficieDienteOdtEvo("4","lingual",Constantes.codigoSectorDiente3);
			lingual.x = 0.5;
			lingual.y = 29.5;
			lingual.rotation = 180;
			this.addChildAt(lingual,0);
			
			//Define la seccion oclusal
			oclusal = new SuperficieDienteOdtEvo("5","oclusal",Constantes.codigoSectorDiente5);
			oclusal.x = 0;
			oclusal.y = 0;			
			this.addChildAt(oclusal,0);
			
			this.gotoAndStop(1);
//			this.useHandCursor = true;
			
			this.isEditado = false;
/*			
			if(activo == Constantes.acronimoSi)
			{
				this.addEventListener(MouseEvent.MOUSE_OVER,sumTamano);			
				this.addEventListener(MouseEvent.MOUSE_OUT,restTamano);
			}
*/
		}
		
		/**
		
		*/
		public function iniciarSelect()
		{
			var ancho:int = 396;
			var largo:int = 41.6;
			
			var myTextFormat:TextFormat = new TextFormat();
			myTextFormat.font = "Arial";
			myTextFormat.color = 0x333333;
			myTextFormat.size = 20;
			
			var odontograma:Object = new Object();
			odontograma = this.parent;
		}
		
				/**
		Inicializa todo el diente, junto sus superficies
		*/
		public function inicializarDiente()
		{
			this.actualizarSuperficie("","");
			this.vestibular.actualizarSuperficie("","");
			this.distal.actualizarSuperficie("","");
			this.mesial.actualizarSuperficie("","");
			this.lingual.actualizarSuperficie("","");
			this.oclusal.actualizarSuperficie("","");

		}
		
		
		/**
			Inicializa los hallazgos 
		*/
		private function inicializarDienteEvent(event:MouseEvent)
		{
			inicializarDiente();
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
		
		/**		
		*/
		public function modificar(event:MouseEvent):void
		{
			if(ausente.selected == false)
			{
				isEditado = true;
				
				this.posx = this.x;
				this.posy = this.y;
				
				this.x = 330;
				this.y = 252;
				this.parent.setChildIndex(this,(this.parent.numChildren-1));
				
				vestibular.scaleY = 3;
				vestibular.scaleX = 3;
				vestibular.x = -305.9;
				vestibular.y = -275.4;
				
				distal.scaleY = 3;
				distal.scaleX = 3;
				distal.x = -388.6;
				distal.y = -193.2;
																
				mesial.scaleY = 3;
				mesial.scaleX = 3;
				mesial.x = -224.6;
				mesial.y = -191.9;
				
				lingual.scaleY = 3;
				lingual.scaleX = 3;
				lingual.x = -304.2;
				lingual.y = -111.4;
				
				oclusal.scaleY = 2.5;
				oclusal.scaleX = 2.5;
				oclusal.x = -307;
				oclusal.y = -194;
				
				addChildAt(vestibular,5)
				addChildAt(distal,5);
				addChildAt(mesial,5);
				addChildAt(lingual,5);
				addChildAt(oclusal,5);

			}
		}
		
		/**
		Dibuja el hallazgo
		*/
		public function dibujarHallazgo(cargo_param:Boolean)
		{
			if(cargo_param && (colorS!= "" || pathImagen!= ""))
			{
				actualizarSuperficie(this.pathImagen,this.colorS);
			}
		}
		
		/**
		Actualiza la informacion de las superficies del diente
		*/
		public function actualizarSuperficie(path:String,colorParam:String)
		{
			this.pathImagen = path;
			this.colorS = colorParam;
			
			if(colorS!= "" || pathImagen!= "")
			{		
				if(colorS!= "")
				{
					setColorDiente();
				}
				else if(pathImagen!= "")
				{
					getImagenHallazgo();
				}
			}
			else
			{
				if(bitmap.bitmapData!=null)
				{
					bitmap.bitmapData.dispose();
					bitmap.bitmapData = null;
				}
			}
		}		
		
		/**
		Obtiene la imagen del hallazgo	
		*/
		public function getImagenHallazgo()
		{
			var loader:Loader;
			loader = new Loader();
			loader.contentLoaderInfo.addEventListener(Event.COMPLETE,completeImagen);				
			loader.load(new URLRequest(this.pathImagen));	
		}
		
		/**
		*/
		public function setColorDiente()
		{
			this.vestibular.setColorS = colorS;
			this.vestibular.dibujarHallazgo(true);
			
			this.distal.setColorS = colorS;
			this.distal.dibujarHallazgo(true);
			
			this.mesial.setColorS = colorS;
			this.mesial.dibujarHallazgo(true);
			
			this.lingual.setColorS = colorS;			
			this.lingual.dibujarHallazgo(true);
			
			this.oclusal.setColorS = colorS;
			this.oclusal.dibujarHallazgo(true);
		}

		
		/**
		*/
		public function completeImagen(event:Event)
		{
			var loaderImg:Loader = Loader(event.target.loader);									
			bitmap = Bitmap(loaderImg.content);

			var diente:Shape = Shape(dienteCompleto.getChildByName("shape"));
			if(diente!=null)
			{
				diente.graphics.clear();
			}
			else
			{
				diente = new Shape();
			}

			var transladar:Matrix = new Matrix();
			
			var bitmapDataOriginal:BitmapData=bitmap.bitmapData;
			var tamanio:int=100;
			var scaleFactor:Number=tamanio/150;
			var newWidth:Number=bitmapDataOriginal.width*scaleFactor;
			var newHeight:Number=bitmapDataOriginal.height*scaleFactor;
			var bitmapDataEscalado:BitmapData=new BitmapData(newWidth,newHeight,true,0xFFFFFFFF);
			transladar.scale(scaleFactor,scaleFactor);
			bitmapDataEscalado.draw(bitmapDataOriginal,transladar);


//			bitmapDataEscalado.threshold(bitmapDataEscalado, new Rectangle(0, 0, tamanio, tamanio), new Point(0, 0), ">", 0xAAAAAA, 0x20002200, 0x000000FF, true);
			bitmap.bitmapData=bitmapDataEscalado;
			bitmap.x=-450
			bitmap.y=-337;

			transladar=new Matrix();
			diente.graphics.beginBitmapFill(bitmap.bitmapData,transladar,false,true);
			diente.graphics.drawCircle(tamanio/2,tamanio/2,tamanio/2);
			diente.graphics.endFill();
			
			if(dienteCompleto.getChildByName("shape")==null)
			{
				diente.name="shape";
				diente.alpha=0.7;
				dienteCompleto.addChild(diente);
			}
			
			loaderImg.unload();
		}		
		
		/**
			inicializa el diente excluyente
		*/
		public function iniciarDienteExcluyente()
		{
			if(this.dienteExcluido > 0)
			{
				
				var odonto = this.parent as Object;
				var dienteEx = odonto.getChildByName(this.dienteExcluido) as DienteOdtEvo;
				
				if(dienteEx != null 
				   && dienteEx.getActivo == Constantes.acronimoSi 
				   	&& dienteEx.esDienteUsado())
				{
					dienteEx.inicializarDiente();
				}
			}
		}
		
		public function get getAusente():CheckBox
		{
			return this.ausente;
		}
		
		public function set setAusente(param_value:CheckBox):void
		{
			this.ausente = param_value;			
		}
		
		public function get getVestibular():SuperficieDienteOdtEvo
		{
			return this.vestibular;
		}
		
		public function get getDistal():SuperficieDienteOdtEvo
		{
			return this.distal;
		}
		
		public function get getMesial():SuperficieDienteOdtEvo
		{
			return this.mesial;
		}
		
		public function get getLingual():SuperficieDienteOdtEvo
		{
			return this.lingual;
		}
		
		public function get getOclusal():SuperficieDienteOdtEvo
		{
			return this.oclusal;
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
		
		public function get getPathImagen():String
		{
			return this.pathImagen;
		}
		
		public function set setPathImagen(param_value:String):void
		{
			this.pathImagen = param_value;
		}
				
		public function get getBitmap():Bitmap
		{
			return this.bitmap;
		}
		
		public function set setBitmap(param_value:Bitmap):void
		{
			this.bitmap = param_value;
		}

		public function get getColorS():String
		{
			return this.colorS;
		}
		
		public function set setColorS(param_value:String):void
		{
			this.colorS = param_value;
		}
	}
}