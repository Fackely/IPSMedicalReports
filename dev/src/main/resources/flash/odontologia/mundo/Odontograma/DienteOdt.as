package mundo.Odontograma
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
	import fl.controls.Label;
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
	
	public class DienteOdt extends MovieClip 
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
		var vestibular:SuperficieDienteOdt;
		
		//Superficie distal
		var distal:SuperficieDienteOdt;
		
		//Superficie mesial
		var mesial:SuperficieDienteOdt;
		
		//Superficie lingual
		var lingual:SuperficieDienteOdt;
		
		//Superficie Oclusal
		var oclusal:SuperficieDienteOdt;
		
		//indica si el diente esta siendo editado
		var isEditado:Boolean;
		
		//Poscion x original
		private var posx:int;
		
		//Posicion y original
		private var posy:int;
		
		//Select para la seccion Vestibular
		var sl_vestibular:ComboBox;
		
		//Select para la seccion Mesial		
		var sl_mesial:ComboBox;
		
		//Select para la seccion Oclusal
	 	var sl_oclusal:ComboBox;
		
		//Select para la seccion Lingual
		var sl_lingual:ComboBox;
		
		//Select para la seccion Distal
		var sl_distal:ComboBox;
		
		//Select para la seccion Todos		
		var sl_todos:ComboBox;
	 
		//Indicador del numero del diente
		private var labelDiente:TextField;
		
		//codigo Hallazgo diente
		private var codigoHallazgo:String;
		
		//nombre Hallazgo diente
		private var descripcionHallazgo:String;
		
		//path Hallazgo diente
		private var pathImagen:String;

		// Borde de la imagen
		private var borde:String;

		//
		private var bitmap:Bitmap;
		
		//
		private var esEvaluado:Boolean;
		
		//
		private var cargarImagenArray:Boolean;
		
		//
		private var codigoConvencion:String;
		
		private var cuadrante:int;
		
		private var modificable:Boolean;
		
		/**
		
		*/
		public function DienteOdt(param_diente:int,param_activo:String,param_excluido:String, cuadrante:int)
		{
			this.bitmap = new Bitmap();
			this.name = param_diente+"";
			this.codigoConvencion = "";
			this.esEvaluado = false;
			this.cuadrante=cuadrante;
			ausente = new CheckBox();
			this.modificable=true;
			
			var seccion1Txt:String="";
			var seccion2Txt:String="";
			var seccion3Txt:String="";
			var seccion4Txt:String="";
			var seccion5Txt:String="";

			switch(cuadrante)
			{
				case 1:
					seccion1Txt="Vestibular";
					seccion2Txt="Mesial";
					seccion3Txt="Palatino";
					seccion4Txt="Distal";
				break;
				case 2:
					seccion1Txt="Vestibular";
					seccion2Txt="Distal";
					seccion3Txt="Palatino";
					seccion4Txt="Mesial";
				break;
				case 3:
					seccion1Txt="Lingual";
					seccion2Txt="Distal";
					seccion3Txt="Vestibular";
					seccion4Txt="Mesial";
				break;
				case 4:
					seccion1Txt="Lingual";
					seccion2Txt="Mesial";
					seccion3Txt="Vestibular";
					seccion4Txt="Distal";
				break;
				default :
					trace("El cuadrante indicado no existe, debe ser de 1 a 4");
			}
			if( (param_diente%10) >=1 &&  (param_diente%10)<=3)
			{
				seccion5Txt="Incisal";
			}
			else
			{
				seccion5Txt="Oclusal";
			}

			numeroDiente = param_diente;
			activo = param_activo;
			excluido = param_excluido;
			dienteExcluido = getNumeroDienteExcluye(param_diente);
			
			//Define la seccion vestibular
			vestibular = new SuperficieDienteOdt("1",seccion1Txt,Constantes.codigoSectorDiente1,Constantes.acronimoNo);
			vestibular.x = 0.5;
			vestibular.y = -28.5;
			this.addChildAt(vestibular,0);
			
			//Define la seccion mesial
			mesial = new SuperficieDienteOdt("2",seccion2Txt,Constantes.codigoSectorDiente2,Constantes.acronimoNo);
			mesial.x = 29;
			mesial.y = 1;
			mesial.rotation = 90;
			this.addChildAt(mesial,0);
			
			//Define la seccion lingual
			lingual = new SuperficieDienteOdt("3",seccion3Txt,Constantes.codigoSectorDiente3,Constantes.acronimoNo);
			lingual.x = -0.5;
			lingual.y = 29.5;
			lingual.rotation = 180;
			this.addChildAt(lingual,0);

			//Define la seccion distal
			distal = new SuperficieDienteOdt("4",seccion4Txt,Constantes.codigoSectorDiente4,Constantes.acronimoNo);
			distal.x = -29;
			distal.y = 0;
			distal.rotation = -90;
			this.addChildAt(distal,0);

			//Define la seccion oclusal
			oclusal = new SuperficieDienteOdt("5",seccion5Txt,Constantes.codigoSectorDiente5,Constantes.acronimoNo);
			oclusal.x = 0;
			oclusal.y = 0;
			this.addChildAt(oclusal,0);
			
			this.gotoAndStop(1);			
			this.useHandCursor = true;
			
			this.isEditado = false;
			
			if(activo == Constantes.acronimoSi)
			{
				this.addEventListener(MouseEvent.MOUSE_OVER,sumTamano);			
				this.addEventListener(MouseEvent.MOUSE_OUT,restTamano);							
				this.addEventListener(MouseEvent.CLICK,modificar);
			}
			
			labelDiente =  new TextField();
			var formatDiente:TextFormat = new TextFormat();
			formatDiente.color = 0x333333;
			formatDiente.font = "Arial";
            formatDiente.size = 94.2;
            formatDiente.bold = true;
            formatDiente.italic = false;						
			labelDiente.defaultTextFormat = formatDiente;	
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

			// Si alguna de las secciones no es modificable, por lo tanto el diente completo tampoco
			var dienteModificable:Boolean=true;
			
			// Si es modificable se llenan todos los campos
			this.sl_vestibular = new ComboBox();
			this.sl_vestibular.name = "sl_vestibular";
			this.sl_vestibular.dataProvider = odontograma.getDpHallazgosSuper;				
			this.sl_vestibular.dropdownWidth = 300;
			this.sl_vestibular.textField.setStyle("textFormat",myTextFormat);
			this.sl_vestibular.width = ancho;
			this.sl_vestibular.height = largo;
			this.sl_vestibular.addEventListener(Event.CHANGE,cambioHallazgo);
			
			this.sl_mesial = new ComboBox();
			this.sl_mesial.name = "sl_mesial";
			this.sl_mesial.dataProvider = odontograma.getDpHallazgosSuper;
			this.sl_mesial.dropdownWidth = 300;
			this.sl_mesial.textField.setStyle("textFormat",myTextFormat);
			this.sl_mesial.width = ancho;
			this.sl_mesial.height = largo;
			this.sl_mesial.addEventListener(Event.CHANGE,cambioHallazgo);
			
			this.sl_oclusal = new ComboBox();
			this.sl_oclusal.name = "sl_oclusal";
			this.sl_oclusal.dataProvider = odontograma.getDpHallazgosSuper;
			this.sl_oclusal.dropdownWidth = 300;
			this.sl_oclusal.textField.setStyle("textFormat",myTextFormat);
			this.sl_oclusal.width = ancho;
			this.sl_oclusal.height = largo;
			this.sl_oclusal.addEventListener(Event.CHANGE,cambioHallazgo);
			
			this.sl_lingual = new ComboBox();
			this.sl_lingual.name = "sl_lingual";
			this.sl_lingual.dataProvider = odontograma.getDpHallazgosSuper;
			this.sl_lingual.dropdownWidth = 300;
			this.sl_lingual.textField.setStyle("textFormat",myTextFormat);
			this.sl_lingual.width = ancho;
			this.sl_lingual.height = largo;
			this.sl_lingual.addEventListener(Event.CHANGE,cambioHallazgo);
			
			this.sl_distal = new ComboBox();
			this.sl_distal.name = "sl_distal";
			this.sl_distal.textField.setStyle("textFormat",myTextFormat);
			this.sl_distal.dropdownWidth = 300;
			this.sl_distal.dataProvider = odontograma.getDpHallazgosSuper;
			this.sl_distal.width = ancho;
			this.sl_distal.height = largo;
			this.sl_distal.addEventListener(Event.CHANGE,cambioHallazgo);
			
			this.sl_todos = new ComboBox();
			this.sl_todos.name = "sl_todos";			
			this.sl_todos.dropdownWidth = 300;			
			this.sl_todos.textField.setStyle("textFormat",myTextFormat);
			this.sl_todos.dataProvider = odontograma.getDpHallazgosDiente;
			this.sl_todos.width = ancho;
			this.sl_todos.height = largo;
			this.sl_todos.addEventListener(Event.CHANGE,cambioHallazgo);
		}
		
				/**
		Inicializa todo el diente, junto sus superficies
		*/
		public function inicializarDiente()
		{
			var modificable:Boolean=true;
			if(this.vestibular.getModificable)
			{
				this.vestibular.actualizarSuperficie(Constantes.codigoNuncaValido+"","","","","");
				sl_vestibular.selectedItem = 0;
			}
			else
			{
				modificable=false;
			}
			if(this.mesial.getModificable)
			{
				this.mesial.actualizarSuperficie(Constantes.codigoNuncaValido+"","","","","");
				sl_mesial.selectedItem = 0;
			}
			else
			{
				modificable=false;
			}
			if(this.distal.getModificable)
			{
				this.distal.actualizarSuperficie(Constantes.codigoNuncaValido+"","","","","");
				sl_distal.selectedItem = 0;
			}
			else
			{
				modificable=false;
			}
			if(this.lingual.getModificable)
			{
				this.lingual.actualizarSuperficie(Constantes.codigoNuncaValido+"","","","","");
				sl_lingual.selectedItem = 0;
			}
			else
			{
				modificable=false;
			}
			if(this.oclusal.getModificable)
			{
				this.oclusal.actualizarSuperficie(Constantes.codigoNuncaValido+"","","","","");
				sl_oclusal.selectedItem = 0;
			}
			else
			{
				modificable=false;
			}
			if(modificable && this.getModificable)
			{
				this.actualizarSuperficie(Constantes.codigoNuncaValido+"","","","","");
				sl_todos.selectedItem = 0;
			}
			this.btn_eliminarhallazgo.visible=false;
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
			if(ausente.selected == false 
			   && !isEditado)
			{			
				this.height = 59;
				this.width = 59;
				this.gotoAndStop(2);
			}
		}
		
		public function restTamano(event:MouseEvent):void
		{
			if(ausente.selected == false 
			   && !isEditado)
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
			if(ausente.selected == false 
			   && !isEditado 
			   	&& !(event.target is Button))
			{					
				isEditado = true;
				
				this.posx = this.x;
				this.posy = this.y;
				
				this.x = 330;
				this.y = 252;
				this.parent.setChildIndex(this,(this.parent.numChildren-1));
				this.gotoAndStop(3);				
				
				labelDiente.text = this.numeroDiente+"";
				labelDiente.x = -574.2;
				labelDiente.y = -390.8;				
				labelDiente.embedFonts = false;
				labelDiente.antiAliasType = AntiAliasType.NORMAL; 				
				labelDiente.selectable = false;
				
				this.addChild(labelDiente);
				
				this.btn_cerraredicion.label = "Actualizar";
				this.btn_cerraredicion.setStyle("textFormat", new TextFormat(null,20,0x333333,true));
				this.btn_cerraredicion.addEventListener(MouseEvent.CLICK,cerrarEdicion);

				this.btn_eliminarhallazgo.label = "Eliminar Hallazgos";
				this.btn_eliminarhallazgo.setStyle("textFormat", new TextFormat(null,20,0x333333,true));
				this.btn_eliminarhallazgo.addEventListener(MouseEvent.CLICK,inicializarDienteEvent);
				this.btn_eliminarhallazgo.visible=false;
				
				this.sl_vestibular.x = 60;
				this.sl_vestibular.y = -340.2;
				
				this.sl_mesial.x = 60;
				this.sl_mesial.y = -259.8;
				
				this.sl_oclusal.x = 60;
				this.sl_oclusal.y = -179.8;
				
				this.sl_lingual.x = 60;
				this.sl_lingual.y = -99.8;
				
				this.sl_distal.x = 60;
				this.sl_distal.y = -19.0;
				
				this.sl_todos.x = 60;
				this.sl_todos.y = 120.2;

				var modificableDiente:Boolean=true;
				var modificableSuperficie:Boolean=true;
				
				if(!this.getModificable)
				{
					modificableDiente=false;
				}
				
				var dataOpcionNoValida:DataProvider=new DataProvider();
				dataOpcionNoValida.addItem( { value: Constantes.codigoNuncaValido,label: "OpciÃ³n no disponible"} );

				if(vestibular.getModificable)
				{
					if(modificableDiente)
					{
						addChild(sl_vestibular);
					}
					else
					{
						sl_vestibular.dataProvider=dataOpcionNoValida;
						addChild(crearLabelSector(vestibular, sl_vestibular));
					}
				}
				else
				{
					addChild(crearLabelSector(vestibular, sl_vestibular));
					modificableSuperficie=false;
				}
				trace(mesial.getModificable);
				if(mesial.getModificable)
				{
					if(modificableDiente)
					{
						addChild(sl_mesial);
					}
					else
					{
						sl_mesial.dataProvider=dataOpcionNoValida;
						addChild(crearLabelSector(mesial, sl_mesial));
					}
				}
				else
				{
					addChild(crearLabelSector(mesial, sl_mesial));
					modificableSuperficie=false;
				}
				if(oclusal.getModificable)
				{
					if(modificableDiente)
					{
						addChild(sl_oclusal);
					}
					else
					{
						sl_oclusal.dataProvider=dataOpcionNoValida;
						addChild(crearLabelSector(oclusal, sl_oclusal));
					}
				}
				else
				{
					addChild(crearLabelSector(oclusal, sl_oclusal));
					modificableSuperficie=false;
				}
				if(lingual.getModificable)
				{
					if(modificableDiente)
					{
						addChild(sl_lingual);
					}
					else
					{
						sl_lingual.dataProvider=dataOpcionNoValida;
						addChild(crearLabelSector(lingual, sl_lingual));
					}
				}
				else
				{
					addChild(crearLabelSector(lingual, sl_lingual));
					modificableSuperficie=false;
				}
				if(distal.getModificable)
				{
					if(modificableDiente)
					{
						addChild(sl_distal);
					}
					else
					{
						sl_distal.dataProvider=dataOpcionNoValida;
						addChild(crearLabelSector(distal, sl_distal));
					}
				}
				else
				{
					addChild(crearLabelSector(distal, sl_distal));
					modificableSuperficie=false;
				}
				if(this.getModificable)
				{
					if(modificableSuperficie)
					{
						addChild(sl_todos);
					}
					else
					{
						sl_todos.dataProvider=dataOpcionNoValida;
						addChild(crearLabelDiente(sl_todos));
					}
				}
				else
				{
					addChild(crearLabelDiente(sl_todos));
				}

				var formatoLabels:TextFormat=new TextFormat();
				formatoLabels.font = "Arial";
				formatoLabels.color = 0x000000;
				formatoLabels.bold=true;
				var scaleFactor:Number=2.8;

				this.lblSeccion1.defaultTextFormat=formatoLabels;
				this.lblSeccion1.text=vestibular.getNombreSector;
				this.lblSeccion1.antiAliasType=flash.text.AntiAliasType.ADVANCED;
				
				vestibular.scaleY = scaleFactor;
				vestibular.scaleX = scaleFactor;
				vestibular.x = -302;
				vestibular.y = -272;

				this.lblSeccion2.defaultTextFormat=formatoLabels;
				this.lblSeccion2.text=mesial.getNombreSector;
				this.lblSeccion2.antiAliasType=flash.text.AntiAliasType.ADVANCED;

				mesial.scaleY = scaleFactor;
				mesial.scaleX = scaleFactor;
				mesial.x = -223;
				mesial.y = -190;

				this.lblSeccion3.defaultTextFormat=formatoLabels;
				this.lblSeccion3.text=lingual.getNombreSector;
				this.lblSeccion3.antiAliasType=flash.text.AntiAliasType.ADVANCED;

				distal.scaleY = scaleFactor;
				distal.scaleX = scaleFactor;
				distal.x = -385;
				distal.y = -193;

				this.lblSeccion4.defaultTextFormat=formatoLabels;
				this.lblSeccion4.text=distal.getNombreSector;
				this.lblSeccion4.antiAliasType=flash.text.AntiAliasType.ADVANCED;

				lingual.scaleY = scaleFactor;
				lingual.scaleX = scaleFactor;
				lingual.x = -304.2;
				lingual.y = -111;

				this.lblSeccion5.defaultTextFormat=formatoLabels;
				this.lblSeccion5.text=oclusal.getNombreSector;
				this.lblSeccion5.antiAliasType=flash.text.AntiAliasType.ADVANCED;

				oclusal.scaleY = scaleFactor;
				oclusal.scaleX = scaleFactor;
				oclusal.x = -303;
				oclusal.y = -193;
				
				addChildAt(vestibular,5)
				addChildAt(distal,5);
				addChildAt(mesial,5);
				addChildAt(lingual,5);
				addChildAt(oclusal,5);
				
				actualizarSelectConValores();
			}
		}
		
		/**		
		Devuelve la posicion del valor dentro del select
		*/
		public function getPosValorSelect(select:ComboBox,codigo_param:int):int
		{
			for(var i=0; i<select.length; i++)
			{				
				if(Number(select.getItemAt(i).value) == codigo_param)
					return i;
			}
			
			return Constantes.codigoNuncaValido;
		}
		
		/**
		Actualiza la informacion de los selects
		*/
		public function actualizarSelectConValores()
		{			
			if(sl_vestibular.selectedItem == null 
					&& Number(vestibular.getCodigoHallazgo) > 0 && vestibular.getModificable)
			{
				sl_vestibular.selectedIndex = getPosValorSelect(sl_vestibular,Number(vestibular.getCodigoHallazgo));
				this.btn_eliminarhallazgo.visible=true;
			}

			if(sl_mesial.selectedItem == null 
					&& Number(mesial.getCodigoHallazgo) > 0 && mesial.getModificable)
			{
				sl_mesial.selectedIndex = getPosValorSelect(sl_mesial,Number(mesial.getCodigoHallazgo));
				this.btn_eliminarhallazgo.visible=true;
			}

			if(sl_oclusal.selectedItem == null 
					&& Number(oclusal.getCodigoHallazgo) > 0 && oclusal.getModificable)
			{
				sl_oclusal.selectedIndex = getPosValorSelect(sl_oclusal,Number(oclusal.getCodigoHallazgo));
				this.btn_eliminarhallazgo.visible=true;
			}
				
			if(sl_lingual.selectedItem == null 
					&& Number(lingual.getCodigoHallazgo) > 0 && lingual.getModificable)
			{
				sl_lingual.selectedIndex = getPosValorSelect(sl_lingual,Number(lingual.getCodigoHallazgo));
				this.btn_eliminarhallazgo.visible=true;
			}
				
			if(sl_distal.selectedItem == null 
					&& Number(distal.getCodigoHallazgo) > 0 && distal.getModificable)
			{
				sl_distal.selectedIndex = getPosValorSelect(sl_distal,Number(distal.getCodigoHallazgo));
				this.btn_eliminarhallazgo.visible=true;
			}
				
			if(sl_todos.selectedItem == null 
					&& Number(this.getCodigoHallazgo) > 0 && this.getModificable)
			{
				sl_todos.selectedIndex = getPosValorSelect(sl_todos,Number(this.getCodigoHallazgo));
				this.btn_eliminarhallazgo.visible=true;
			}
		}
		
		/**
		*/
		public function cerrarEdicion(event:MouseEvent):void
		{
			this.removeChild(this.getChildByName("sl_vestibular"));
			this.removeChild(this.getChildByName("sl_mesial"));
			this.removeChild(this.getChildByName("sl_oclusal"));
			this.removeChild(this.getChildByName("sl_lingual"));
			this.removeChild(this.getChildByName("sl_distal"));
			this.removeChild(this.getChildByName("sl_todos"));	
			
			this.removeChild(labelDiente);			
			isEditado = false;
			
			vestibular.scaleY = 1;
			vestibular.scaleX = 1;
			vestibular.x = 0.5;
			vestibular.y = -28.5;
			this.addChildAt(vestibular,0);

			mesial.scaleY = 1;
			mesial.scaleX = 1;			
			mesial.x = 29;
			mesial.y = 1;
			mesial.rotation = 90;
			this.addChildAt(mesial,0);

			lingual.scaleY = 1;
			lingual.scaleX = 1;			
			lingual.x = -0.5;
			lingual.y = 29.5;
			lingual.rotation = 180;
			this.addChildAt(lingual,0);		

			distal.scaleY = 1;
			distal.scaleX = 1;			
			distal.x = -29;
			distal.y = 0;
			distal.rotation = -90;
			this.addChildAt(distal,0);
			
			oclusal.scaleY = 1;
			oclusal.scaleX = 1;			
			oclusal.x = 0;
			oclusal.y = 0;
			this.addChildAt(oclusal,0);		
			
			this.gotoAndStop(1);
			this.x = this.posx;
			this.y = this.posy;
			this.height = 34;
			this.width = 34;
		}
		
		
		/**
		Dibuja el hallazgo
		*/
		public function dibujarHallazgo(cargo_param:Boolean)
		{
			cargarImagenArray = cargo_param;
			if(cargarImagenArray)						
			{
				getImagenHallazgo();
			}
		}		
		
		/**
		
		*/
		public function cambioHallazgo(e:Event)
		{
			var cb:ComboBox = e.currentTarget as ComboBox;
		    var item:Object = cb.selectedItem;			
						
			if(cb.name == "sl_todos")
			{
				this.vestibular.actualizarSuperficie(Constantes.codigoNuncaValido+"","","","","");
				this.distal.actualizarSuperficie(Constantes.codigoNuncaValido+"","","","","");
				this.mesial.actualizarSuperficie(Constantes.codigoNuncaValido+"","","","","");
				this.lingual.actualizarSuperficie(Constantes.codigoNuncaValido+"","","","","");
				this.oclusal.actualizarSuperficie(Constantes.codigoNuncaValido+"","","","","");
				this.actualizarSuperficie(item.value,item.label,item.path,item.convencion,item.borde);
			}
			else
			{
				if(cb.name == "sl_vestibular")
				{
					this.vestibular.actualizarSuperficie(item.value,item.label,item.path,item.convencion, item.borde);
				}
				else if(cb.name == "sl_distal")
				{
					this.distal.actualizarSuperficie(item.value,item.label,item.path,item.convencion, item.borde);
				}
				else if(cb.name == "sl_mesial")
				{
					this.mesial.actualizarSuperficie(item.value,item.label,item.path,item.convencion, item.borde);
				}					
				else if(cb.name == "sl_lingual")
				{
					this.lingual.actualizarSuperficie(item.value,item.label,item.path,item.convencion, item.borde);
				}
				else if(cb.name == "sl_oclusal")
				{
					this.oclusal.actualizarSuperficie(item.value,item.label,item.path,item.convencion, item.borde);
				}
				
				//Verifica si estaba seleccionado todo el diente
				if(Number(this.codigoHallazgo) > 0)
				{
					if(cb.name != "sl_vestibular")
						this.vestibular.actualizarSuperficie(Constantes.codigoNuncaValido+"","","","","");
					if(cb.name != "sl_distal")
						this.distal.actualizarSuperficie(Constantes.codigoNuncaValido+"","","","","");
					if(cb.name != "sl_mesial")
						this.mesial.actualizarSuperficie(Constantes.codigoNuncaValido+"","","","","");
					if(cb.name != "sl_lingual")	
						this.lingual.actualizarSuperficie(Constantes.codigoNuncaValido+"","","","","");
					if(cb.name != "sl_oclusal")	
						this.oclusal.actualizarSuperficie(Constantes.codigoNuncaValido+"","","","","");
				}
				
				this.actualizarSuperficie(Constantes.codigoNuncaValido+"","","","","");
			}
			if(
			   		(sl_vestibular.selectedItem==null || sl_vestibular.selectedItem.value <= 0)
				&&
			   		(sl_mesial.selectedItem==null || sl_mesial.selectedItem.value <= 0)
				&&
			   		(sl_lingual.selectedItem==null || sl_lingual.selectedItem.value <= 0)
				&&
			   		(sl_distal.selectedItem==null || sl_distal.selectedItem.value <= 0)
				&&
			   		(sl_oclusal.selectedItem==null || sl_oclusal.selectedItem.value <= 0)
				&&
			   		(sl_todos.selectedItem==null || sl_todos.selectedItem.value <= 0)
			   )
			{
				this.btn_eliminarhallazgo.visible=false;
			}
			else
			{
				this.btn_eliminarhallazgo.visible=true;
			}
			
			iniciarDienteExcluyente();
		}
		
		/**
		Actualiza la informacion de las superficies del diente
		*/
		public function actualizarSuperficie(codigo:String,nombre:String,path:String,convencion:String,borde:String)
		{
			this.codigoHallazgo = codigo;
			this.descripcionHallazgo = nombre;
			this.pathImagen = path;
			this.borde = borde;
			this.codigoConvencion = convencion;
			
			if(Number(this.codigoHallazgo) <= 0 || this.pathImagen=="")
			{
				sl_todos.selectedItem = 0;
				
				if(bitmap.bitmapData!=null)
				{
					bitmap.bitmapData.dispose();
					bitmap.bitmapData = null;
					var diente:Shape = Shape(dienteCompleto.getChildByName("shape"));
					if(diente!=null)
					{
						diente.graphics.clear();
					}
				}
			}
			else
			{
				sl_vestibular.selectedItem = 0;
				sl_distal.selectedItem = 0;
				sl_mesial.selectedItem = 0;
				sl_lingual.selectedItem = 0;
				sl_oclusal.selectedItem = 0;
				getImagenHallazgo();
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
			var tamanio:int=87;
			var scaleFactor:Number=tamanio/150;
			var newWidth:Number=bitmapDataOriginal.width*scaleFactor;
			var newHeight:Number=bitmapDataOriginal.height*scaleFactor;
			var bitmapDataEscalado:BitmapData=new BitmapData(newWidth,newHeight,true,0xFFFFFFFF);
			transladar.scale(scaleFactor,scaleFactor);
			transladar.translate(7.5,7.5);
			bitmapDataEscalado.draw(bitmapDataOriginal,transladar);


			//bitmapDataEscalado.threshold(bitmapDataEscalado, new Rectangle(0, 0, tamanio, tamanio), new Point(0, 0), ">", 0xAAAAAA, 0x20002200, 0x00FF0000, true);
			bitmap.bitmapData=bitmapDataEscalado;

			transladar=new Matrix();
			diente.graphics.beginBitmapFill(bitmap.bitmapData,transladar,false,true);

			if(borde!=null && borde!='')
			{
				trace("borde "+borde+" "+uint(borde));
				diente.graphics.lineStyle(3, uint(borde));
			}
			diente.graphics.drawCircle(51,51,tamanio/2);
			diente.graphics.endFill();
			
			if(dienteCompleto.getChildByName("shape")==null)
			{
				diente.name="shape";
				diente.alpha=0.8;
				dienteCompleto.addChild(diente);
			}
			
			loaderImg.unload();
		}		
		
		
		/**		
		verifica si el diente posee algun hallazgo
		*/
		public function esDienteUsado():Boolean
		{
			if(Number(this.codigoHallazgo) > 0 
				|| Number(this.vestibular.getCodigoHallazgo) > 0
				|| Number(this.distal.getCodigoHallazgo) > 0
				|| Number(this.mesial.getCodigoHallazgo) > 0
				|| Number(this.lingual.getCodigoHallazgo) > 0
				|| Number(this.oclusal.getCodigoHallazgo) > 0)
			{
				return true;
			}
			
			return false;
		}
		
		/**
			inicializa el diente excluyente
		*/
		public function iniciarDienteExcluyente()
		{
			if(this.dienteExcluido > 0)
			{
				
				var odonto = this.parent as Object;
				var dienteEx = odonto.getChildByName(this.dienteExcluido) as DienteOdt;
				
				if(dienteEx != null 
				   && dienteEx.getActivo == Constantes.acronimoSi 
				   	&& dienteEx.esDienteUsado())
				{
					dienteEx.inicializarDiente();
				}
			}
		}
		
		public function crearLabelSector(superficie:SuperficieDienteOdt, sl_superficie:ComboBox):TextField
		{
			var label:TextField=new TextField();
			label.type="input";
			label.name=sl_superficie.name;
			label.text=OdontogramaDx.obtenerDataProviderXCodigo(sl_superficie.dataProvider, superficie.getCodigoHallazgo);
			label.x=sl_superficie.x+15;
			label.y=sl_superficie.y+5;
			label.width=sl_superficie.width;
			label.height=sl_superficie.height;
			var myTextFormat:TextFormat = new TextFormat();
			myTextFormat.font = "Arial";
			myTextFormat.color = 0x333333;
			myTextFormat.size = 22;
			label.setTextFormat(myTextFormat);
			label.type="dynamic";
			return label;
		}

		public function crearLabelDiente(sl_diente:ComboBox):TextField
		{
			var label:TextField=new TextField();
			label.type="input";
			label.name=sl_diente.name;
			label.text=OdontogramaDx.obtenerDataProviderXCodigo(sl_diente.dataProvider, this.getCodigoHallazgo);
			label.x=sl_diente.x+15;
			label.y=sl_diente.y+5;
			label.width=sl_diente.width;
			label.height=sl_diente.height;
			var myTextFormat:TextFormat = new TextFormat();
			myTextFormat.font = "Arial";
			myTextFormat.color = 0x333333;
			myTextFormat.size = 22;
			label.setTextFormat(myTextFormat);
			label.type="dynamic";
			return label;
		}

		public function get getAusente():CheckBox
		{
			return this.ausente;
		}
		
		public function set setAusente(param_value:CheckBox):void
		{
			this.ausente = param_value;			
		}
		
		public function get getVestibular():SuperficieDienteOdt
		{
			return this.vestibular;
		}
		
		public function get getDistal():SuperficieDienteOdt
		{
			return this.distal;
		}
		
		public function get getMesial():SuperficieDienteOdt
		{
			return this.mesial;
		}
		
		public function get getLingual():SuperficieDienteOdt
		{
			return this.lingual;
		}
		
		public function get getOclusal():SuperficieDienteOdt
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
		
		public function get getCodigoHallazgo():String
		{
			return this.codigoHallazgo;
		}
		
		public function set setCodigoHallazgo(param_value:String):void
		{
			this.codigoHallazgo = param_value;
		}
		
		public function get getDescripcionHallazgo():String
		{
			return this.descripcionHallazgo;
		}
		
		public function set setDescripcionHallazgo(param_value:String):void
		{
			this.descripcionHallazgo = param_value;
		}
		
		public function get getPathImagen():String
		{
			return this.pathImagen;
		}
		
		public function set setPathImagen(param_value:String):void
		{
			this.pathImagen = param_value;
		}
		
		public function get getEsEvaluado():Boolean
		{
			return this.esEvaluado;
		}
		
		public function set setEsEvaluado(param_value:Boolean):void
		{
			this.esEvaluado = param_value;
		}
		
		public function get getBitmap():Bitmap
		{
			return this.bitmap;
		}
		
		public function set setBitmap(param_value:Bitmap):void
		{
			this.bitmap = param_value;
		}
		
		public function get getCodigoConvencion():String
		{
			return this.codigoConvencion;
		}
		
		public function set setCodigoConvencion(param_value:String):void
		{
			this.codigoConvencion = param_value;
		}	

		public function get getCuadrante():int
		{
			return this.cuadrante;
		}
		
		public function set setCuadrante(param_value:int):void
		{
			this.cuadrante = param_value;
		}

		public function get getModificable():Boolean
		{
			return this.modificable;
		}
		
		public function set setModificable(modificable:Boolean):void
		{
			this.modificable=modificable;
		}
		
		public function get getSlVestibular():ComboBox
		{
			return this.sl_vestibular;
		}

		public function get getSlMesial():ComboBox
		{
			return this.sl_mesial;
		}

		public function get getSlLingual():ComboBox
		{
			return this.sl_lingual;
		}

		public function get getSlDistal():ComboBox
		{
			return this.sl_distal;
		}

		public function get getBorde():String
		{
			return this.borde;
		}

		public function set setBorde(borde:String):void
		{
			this.borde = borde;
		}

	}
}