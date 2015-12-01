package mundo.agendaOdontologica                
{
	import flash.display.MovieClip;
	import flash.text.TextField;
    import flash.text.TextFieldAutoSize;
	import flash.text.TextFormat;
	import flash.external.ExternalInterface;
	import flash.text.AntiAliasType;
	import flash.display.Sprite;
	import flash.display.LineScaleMode;
	import flash.geom.Rectangle;	
	import fl.containers.ScrollPane;
	import fl.events.ScrollEvent;
	import fl.controls.ScrollBarDirection;
	import fl.controls.UIScrollBar;

	import flash.display.CapsStyle;
	import flash.display.JointStyle;
	import flash.display.DisplayObject;
	
	import flash.events.MouseEvent;
	import flash.events.Event;
	import flash.utils.Timer;
    import flash.events.TimerEvent;
    import flash.events.Event;
	
	import util.general.Scroll;
	import util.general.Constantes;
		
	public class AgendaOdontologica extends MovieClip
	{
		
		// Constants:
		// Public Properties:
		public var mc_contenido = new MovieClip();
		
		// Private Properties:
		private var xmlAgenda:String = "" ;
		private var xmlConsultorios:String = "" ;
		private var xmlParametros:String = "" ;	
		private var xmlSubAgenda:String = "" ;			
		
		private var labelMensajes:TextField;
		private var formatMensajes:TextFormat;
		
		private var anchoEscenario:int = 0;
		private var largoEscenario:int = 0;
		private var espacioEntreElementos:int = 2;
		
		//minimo tamaño para el largo del intervalo es de 30
		private var largoIntervalo:int = 100;
		private var anchoConsultorio:int = 150;
		
		private var posxIniciaConsultorio:int = 65;
		private var posyIniciaRango:int = 45;
		
		private var paramAgenda:ParametrosAgenda;
		
		private var ct_rangohoras:Sprite;
		private var ct_consultorios:Sprite;
		private var ct_agendaOdo:Sprite;
		private var ct_infoagenda:Sprite;
		private var ct_infocita:Sprite;
		
		private var horientacionNav:int = 0;
		private var incremetoNav:int = 10;
		private var menuNavegacionActivo:Boolean;
		
		// Initialization:
		public function AgendaOdontologica() 
		{
			reset();			
			
			menuNavegacionActivo = true;
			labelMensajes =  new TextField();
			formatMensajes = new TextFormat();
			formatMensajes.color = 0x000000;
            formatMensajes.size = 11;
            formatMensajes.bold = true;
            formatMensajes.italic = false;						
			labelMensajes.defaultTextFormat = formatMensajes;			
			
			anchoEscenario = 800;
			largoEscenario = 600;
			
			ExternalInterface.addCallback("setswfxmlagenda",setswfxmlagenda);
			ExternalInterface.addCallback("setswfxmlconsultorios",setswfxmlconsultorios);
			ExternalInterface.addCallback("setswfxmlparametros",setswfxmlparametros);
			ExternalInterface.addCallback("setswfpintar",setswfpintar);
			
			ExternalInterface.addCallback("setswfxmlcitagenda",setswfxmlcitagenda);
			ExternalInterface.addCallback("setswfpintaragenda",setswfpintarsubagenda);
			
			ct_rangohoras = new Sprite();
			ct_consultorios = new Sprite();
			ct_agendaOdo= new Sprite();
			
			//Inicializa los contenedores
			iniciarContenedores();
/*						
			xmlAgenda = '<Contenido><Agenda><codigo_pk>681</codigo_pk><pos_agenda>0</pos_agenda><color>ff66cc</color><hora_inicio>8</hora_inicio><min_inicio>0</min_inicio><hora_final>20</hora_final><min_final>0</min_final><codigo_consultorio>101</codigo_consultorio><pos_consultorio>1</pos_consultorio><profesional></profesional><especialidad_prof></especialidad_prof><Citas numcitas="5"><SubAgenda hora_ini="12" min_ini="20" hora_fin="12" min_fin="40"><DescripcionCita><codigo_pk_cita>82</codigo_pk_cita><pos_cita>0</pos_cita><hora_fin>12</hora_fin><min_fin>40</min_fin><duracion>20</duracion><estado>Asignado(a)</estado><paciente>Yepes Gonzalo</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>706</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda><SubAgenda hora_ini="14" min_ini="20" hora_fin="14" min_fin="40"><DescripcionCita><codigo_pk_cita>70</codigo_pk_cita><pos_cita>1</pos_cita><hora_fin>14</hora_fin><min_fin>40</min_fin><duracion>20</duracion><estado>Asignado(a)</estado><paciente>Gomez Camila</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>705</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda><SubAgenda hora_ini="16" min_ini="0" hora_fin="16" min_fin="20"><DescripcionCita><codigo_pk_cita>73</codigo_pk_cita><pos_cita>2</pos_cita><hora_fin>16</hora_fin><min_fin>20</min_fin><duracion>20</duracion><estado>Reservado(a)</estado><paciente>Lopez Guillermo</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>703</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda><SubAgenda hora_ini="16" min_ini="40" hora_fin="17" min_fin="0"><DescripcionCita><codigo_pk_cita>79</codigo_pk_cita><pos_cita>3</pos_cita><hora_fin>17</hora_fin><min_fin>0</min_fin><duracion>20</duracion><estado>Asignado(a)</estado><paciente>Yepes Gonzalo</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>706</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda><SubAgenda hora_ini="19" min_ini="0" hora_fin="19" min_fin="20"><DescripcionCita><codigo_pk_cita>71</codigo_pk_cita><pos_cita>4</pos_cita><hora_fin>19</hora_fin><min_fin>20</min_fin><duracion>20</duracion><estado>Cancelada(o)</estado><paciente>Perez Manuel</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>704</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda></Citas></Agenda><Agenda><codigo_pk>628</codigo_pk><pos_agenda>1</pos_agenda><color>0033ff</color><hora_inicio>8</hora_inicio><min_inicio>0</min_inicio><hora_final>20</hora_final><min_final>0</min_final><codigo_consultorio>103</codigo_consultorio><pos_consultorio>2</pos_consultorio><profesional>prOf prOf</profesional><especialidad_prof>HIGIENE  ORAL</especialidad_prof><Citas numcitas="3"><SubAgenda hora_ini="12" min_ini="40" hora_fin="19" min_fin="20"><DescripcionCita><codigo_pk_cita>72</codigo_pk_cita><pos_cita>0</pos_cita><hora_fin>13</hora_fin><min_fin>10</min_fin><duracion>30</duracion><estado>Reservado(a)</estado><paciente>hellO hhellOO</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>800</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda><SubAgenda hora_ini="16" min_ini="0" hora_fin="19" min_fin="20"><DescripcionCita><codigo_pk_cita>84</codigo_pk_cita><pos_cita>1</pos_cita><hora_fin>16</hora_fin><min_fin>30</min_fin><duracion>30</duracion><estado>Reservado(a)</estado><paciente>lalala jiwjdiwd</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>806</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda><SubAgenda hora_ini="17" min_ini="40" hora_fin="19" min_fin="20"><DescripcionCita><codigo_pk_cita>85</codigo_pk_cita><pos_cita>2</pos_cita><hora_fin>18</hora_fin><min_fin>10</min_fin><duracion>30</duracion><estado>Reservado(a)</estado><paciente>trtth htthth</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>807</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda></Citas></Agenda><Agenda><codigo_pk>744</codigo_pk><pos_agenda>2</pos_agenda><color>3366ff</color><hora_inicio>8</hora_inicio><min_inicio>0</min_inicio><hora_final>20</hora_final><min_final>0</min_final><codigo_consultorio>105</codigo_consultorio><pos_consultorio>3</pos_consultorio><profesional>Sonria Sonria</profesional><especialidad_prof>HIGIENE  ORAL</especialidad_prof><Citas numcitas="5"><SubAgenda hora_ini="13" min_ini="20" hora_fin="19" min_fin="20"><DescripcionCita><codigo_pk_cita>69</codigo_pk_cita><pos_cita>0</pos_cita><hora_fin>13</hora_fin><min_fin>50</min_fin><duracion>30</duracion><estado>Reservado(a)</estado><paciente>hellO hhellOO</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>800</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda><SubAgenda hora_ini="13" min_ini="40" hora_fin="19" min_fin="20"><DescripcionCita><codigo_pk_cita>78</codigo_pk_cita><pos_cita>1</pos_cita><hora_fin>14</hora_fin><min_fin>10</min_fin><duracion>30</duracion><estado>Reservado(a)</estado><paciente>mMmmM PlOpm</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>803</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda><SubAgenda hora_ini="14" min_ini="40" hora_fin="19" min_fin="20"><DescripcionCita><codigo_pk_cita>81</codigo_pk_cita><pos_cita>2</pos_cita><hora_fin>15</hora_fin><min_fin>10</min_fin><duracion>30</duracion><estado>Reservado(a)</estado><paciente>CastrO Lorena</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>804</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda><SubAgenda hora_ini="18" min_ini="0" hora_fin="19" min_fin="20"><DescripcionCita><codigo_pk_cita>75</codigo_pk_cita><pos_cita>3</pos_cita><hora_fin>18</hora_fin><min_fin>30</min_fin><duracion>30</duracion><estado>Reservado(a)</estado><paciente>Gutierrez Carlos</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>801</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda><SubAgenda hora_ini="18" min_ini="20" hora_fin="19" min_fin="20"><DescripcionCita><codigo_pk_cita>77</codigo_pk_cita><pos_cita>4</pos_cita><hora_fin>18</hora_fin><min_fin>50</min_fin><duracion>30</duracion><estado>Reservado(a)</estado><paciente>Plam Plusssssss</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>802</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda></Citas></Agenda><Agenda><codigo_pk>797</codigo_pk><pos_agenda>3</pos_agenda><color>ff66cc</color><hora_inicio>8</hora_inicio><min_inicio>0</min_inicio><hora_final>20</hora_final><min_final>0</min_final><codigo_consultorio>106</codigo_consultorio><pos_consultorio>4</pos_consultorio><profesional>Urrea Melissa</profesional><especialidad_prof>ENDODONCIA</especialidad_prof><Citas numcitas="4"><SubAgenda hora_ini="12" min_ini="0" hora_fin="19" min_fin="20"><DescripcionCita><codigo_pk_cita>64</codigo_pk_cita><pos_cita>0</pos_cita><hora_fin>12</hora_fin><min_fin>20</min_fin><duracion>20</duracion><estado>Asignado(a)</estado><paciente>Sanchez Adriana</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>700</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda><SubAgenda hora_ini="17" min_ini="0" hora_fin="19" min_fin="20"><DescripcionCita><codigo_pk_cita>74</codigo_pk_cita><pos_cita>1</pos_cita><hora_fin>17</hora_fin><min_fin>20</min_fin><duracion>20</duracion><estado>Asignado(a)</estado><paciente>Sanchez Adriana</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>700</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda><SubAgenda hora_ini="19" min_ini="0" hora_fin="19" min_fin="20"><DescripcionCita><codigo_pk_cita>87</codigo_pk_cita><pos_cita>2</pos_cita><hora_fin>19</hora_fin><min_fin>20</min_fin><duracion>20</duracion><estado>Asignado(a)</estado><paciente>Yepes Gonzalo</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>706</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda><SubAgenda hora_ini="19" min_ini="40" hora_fin="20" min_fin="0"><DescripcionCita><codigo_pk_cita>66</codigo_pk_cita><pos_cita>3</pos_cita><hora_fin>20</hora_fin><min_fin>0</min_fin><duracion>20</duracion><estado>Cancelada(o)</estado><paciente>Duarte Alexander</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>701</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda></Citas></Agenda><Agenda><codigo_pk>565</codigo_pk><pos_agenda>4</pos_agenda><color>ff33ff</color><hora_inicio>8</hora_inicio><min_inicio>0</min_inicio><hora_final>20</hora_final><min_final>0</min_final><codigo_consultorio>104</codigo_consultorio><pos_consultorio>5</pos_consultorio><profesional>Aguilar Paula</profesional><especialidad_prof>HIGIENE  ORAL</especialidad_prof><Citas numcitas="5"><SubAgenda hora_ini="12" min_ini="0" hora_fin="20" min_fin="0"><DescripcionCita><codigo_pk_cita>68</codigo_pk_cita><pos_cita>0</pos_cita><hora_fin>12</hora_fin><min_fin>20</min_fin><duracion>20</duracion><estado>Reservado(a)</estado><paciente>hellO hhellOO</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>800</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda><SubAgenda hora_ini="13" min_ini="40" hora_fin="20" min_fin="0"><DescripcionCita><codigo_pk_cita>76</codigo_pk_cita><pos_cita>1</pos_cita><hora_fin>14</hora_fin><min_fin>20</min_fin><duracion>40</duracion><estado>Reservado(a)</estado><paciente>Yepes Gonzalo</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>706</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda><SubAgenda hora_ini="14" min_ini="20" hora_fin="20" min_fin="0"><DescripcionCita><codigo_pk_cita>65</codigo_pk_cita><pos_cita>2</pos_cita><hora_fin>14</hora_fin><min_fin>40</min_fin><duracion>20</duracion><estado>Cancelada(o)</estado><paciente>Duarte Alexander</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>701</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda><SubAgenda hora_ini="15" min_ini="40" hora_fin="20" min_fin="0"><DescripcionCita><codigo_pk_cita>67</codigo_pk_cita><pos_cita>3</pos_cita><hora_fin>16</hora_fin><min_fin>0</min_fin><duracion>20</duracion><estado>Asignado(a)</estado><paciente>Lopez Guillermo</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>703</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda><SubAgenda hora_ini="19" min_ini="20" hora_fin="20" min_fin="0"><DescripcionCita><codigo_pk_cita>80</codigo_pk_cita><pos_cita>4</pos_cita><hora_fin>20</hora_fin><min_fin>0</min_fin><duracion>40</duracion><estado>Reservado(a)</estado><paciente>Yepes Gonzalo</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>706</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda></Citas></Agenda><Agenda><codigo_pk>334</codigo_pk><pos_agenda>5</pos_agenda><color>ffcccc</color><hora_inicio>8</hora_inicio><min_inicio>0</min_inicio><hora_final>20</hora_final><min_final>0</min_final><codigo_consultorio>102</codigo_consultorio><pos_consultorio>6</pos_consultorio><profesional>Duarte Alexander</profesional><especialidad_prof>IMPLANTOLOGIA</especialidad_prof><Citas numcitas="8"><SubAgenda hora_ini="8" min_ini="0" hora_fin="20" min_fin="0"><DescripcionCita><codigo_pk_cita>56</codigo_pk_cita><pos_cita>0</pos_cita><hora_fin>9</hora_fin><min_fin>40</min_fin><duracion>20</duracion><estado>Asignado(a)</estado><paciente>Sanchez Adriana</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>700</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda><SubAgenda hora_ini="17" min_ini="0" hora_fin="20" min_fin="0"><DescripcionCita><codigo_pk_cita>61</codigo_pk_cita><pos_cita>1</pos_cita><hora_fin>17</hora_fin><min_fin>20</min_fin><duracion>20</duracion><estado>A Reprogramar</estado><paciente>Sanchez Adriana</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>700</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda><SubAgenda hora_ini="18" min_ini="0" hora_fin="20" min_fin="0"><DescripcionCita><codigo_pk_cita>58</codigo_pk_cita><pos_cita>2</pos_cita><hora_fin>18</hora_fin><min_fin>20</min_fin><duracion>20</duracion><estado>A Reprogramar</estado><paciente>Sanchez Adriana</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>700</num_iden_paciente><observaciones></observaciones></DescripcionCita><DescripcionCita><codigo_pk_cita>58</codigo_pk_cita><pos_cita>2</pos_cita><hora_fin>18</hora_fin><min_fin>20</min_fin><duracion>20</duracion><estado>A Reprogramar</estado><paciente>Sanchez Adriana</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>700</num_iden_paciente><observaciones></observaciones></DescripcionCita><DescripcionCita><codigo_pk_cita>58</codigo_pk_cita><pos_cita>2</pos_cita><hora_fin>18</hora_fin><min_fin>20</min_fin><duracion>20</duracion><estado>A Reprogramar</estado><paciente>Sanchez Adriana</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>700</num_iden_paciente><observaciones></observaciones></DescripcionCita><DescripcionCita><codigo_pk_cita>58</codigo_pk_cita><pos_cita>2</pos_cita><hora_fin>18</hora_fin><min_fin>20</min_fin><duracion>20</duracion><estado>A Reprogramar</estado><paciente>Sanchez Adriana</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>700</num_iden_paciente><observaciones></observaciones></DescripcionCita><DescripcionCita><codigo_pk_cita>57</codigo_pk_cita><pos_cita>3</pos_cita><hora_fin>18</hora_fin><min_fin>20</min_fin><duracion>20</duracion><estado>A Reprogramar</estado><paciente>Sanchez Adriana</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>700</num_iden_paciente><observaciones></observaciones></DescripcionCita><DescripcionCita><codigo_pk_cita>59</codigo_pk_cita><pos_cita>4</pos_cita><hora_fin>18</hora_fin><min_fin>20</min_fin><duracion>20</duracion><estado>A Reprogramar</estado><paciente>Sanchez Adriana</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>700</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda><SubAgenda hora_ini="18" min_ini="20" hora_fin="20" min_fin="0"><DescripcionCita><codigo_pk_cita>60</codigo_pk_cita><pos_cita>5</pos_cita><hora_fin>18</hora_fin><min_fin>40</min_fin><duracion>20</duracion><estado>A Reprogramar</estado><paciente>Sanchez Adriana</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>700</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda><SubAgenda hora_ini="18" min_ini="40" hora_fin="20" min_fin="0"><DescripcionCita><codigo_pk_cita>62</codigo_pk_cita><pos_cita>6</pos_cita><hora_fin>19</hora_fin><min_fin>0</min_fin><duracion>20</duracion><estado>A Reprogramar</estado><paciente>Sanchez Adriana</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>700</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda><SubAgenda hora_ini="19" min_ini="40" hora_fin="20" min_fin="0"><DescripcionCita><codigo_pk_cita>63</codigo_pk_cita><pos_cita>7</pos_cita><hora_fin>20</hora_fin><min_fin>0</min_fin><duracion>20</duracion><estado>A Reprogramar</estado><paciente>Sanchez Adriana</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>700</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda></Citas></Agenda></Contenido>';
						
			xmlConsultorios = "<Consultorios><Consultorio><nombre>Consultorio Endodoncia</nombre><codigo_consultorio>101</codigo_consultorio></Consultorio><Consultorio><nombre>ConsultOriO_Mp</nombre><codigo_consultorio>103</codigo_consultorio></Consultorio><Consultorio><nombre>ConsultoriO_PM</nombre><codigo_consultorio>105</codigo_consultorio></Consultorio><Consultorio><nombre>Endondocia</nombre><codigo_consultorio>106</codigo_consultorio></Consultorio><Consultorio><nombre>Higiene oral</nombre><codigo_consultorio>104</codigo_consultorio></Consultorio><Consultorio><nombre>Implantologia</nombre><codigo_consultorio>102</codigo_consultorio></Consultorio></Consultorios>";

			xmlParametros = "<Contenido><Parametros><intervalo>20</intervalo><hora_inicio>8</hora_inicio><min_inicio>0</min_inicio><hora_final>20</hora_final><min_final>0</min_final></Parametros></Contenido>"
			
			setswfpintar();	
			
			xmlSubAgenda = '<Contenido><Agenda><codigo_pk>681</codigo_pk><pos_agenda>0</pos_agenda><color>ff66cc</color><hora_inicio>8</hora_inicio><min_inicio>0</min_inicio><hora_final>20</hora_final><min_final>0</min_final><codigo_consultorio>101</codigo_consultorio><pos_consultorio>1</pos_consultorio><profesional></profesional><especialidad_prof></especialidad_prof><Citas numcitas="5"><SubAgenda hora_ini="12" min_ini="20" hora_fin="12" min_fin="40"><DescripcionCita><codigo_pk_cita>82</codigo_pk_cita><pos_cita>0</pos_cita><hora_fin>12</hora_fin><min_fin>40</min_fin><duracion>20</duracion><estado>AsignadoXXX(a)</estado><paciente>Yepes Gonzalo</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>706</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda><SubAgenda hora_ini="14" min_ini="20" hora_fin="14" min_fin="40"><DescripcionCita><codigo_pk_cita>70</codigo_pk_cita><pos_cita>1</pos_cita><hora_fin>14</hora_fin><min_fin>40</min_fin><duracion>20</duracion><estado>Asignado(a)</estado><paciente>XXXXXX</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>705</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda><SubAgenda hora_ini="16" min_ini="0" hora_fin="16" min_fin="20"><DescripcionCita><codigo_pk_cita>73</codigo_pk_cita><pos_cita>2</pos_cita><hora_fin>16</hora_fin><min_fin>20</min_fin><duracion>20</duracion><estado>Reservado(a)</estado><paciente>Lopez Guillermo</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>703</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda><SubAgenda hora_ini="16" min_ini="40" hora_fin="17" min_fin="0"><DescripcionCita><codigo_pk_cita>79</codigo_pk_cita><pos_cita>3</pos_cita><hora_fin>17</hora_fin><min_fin>0</min_fin><duracion>20</duracion><estado>Asignado(a)</estado><paciente>Yepes Gonzalo</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>706</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda><SubAgenda hora_ini="19" min_ini="0" hora_fin="19" min_fin="20"><DescripcionCita><codigo_pk_cita>71</codigo_pk_cita><pos_cita>4</pos_cita><hora_fin>19</hora_fin><min_fin>20</min_fin><duracion>20</duracion><estado>Cancelada(o)</estado><paciente>Perez Manuel</paciente><tipo_iden_paciente>CC</tipo_iden_paciente><num_iden_paciente>704</num_iden_paciente><observaciones></observaciones></DescripcionCita></SubAgenda></Citas></Agenda></Contenido>';
			setswfpintarsubagenda();*/
		}
		
		
		/**
		Pinta la agenda
		*/
		public function setswfpintar()
		{
			if(this.xmlAgenda!= "" && 
			   	this.xmlConsultorios != "" && 
					this.xmlParametros != "" )
			{
				iniciarContenedores();
				cargarParametros();
				pintarRangos();
				pintarConsultorios();
				pintarAgendaXml(xmlAgenda,false);				
				iniciarNavegacion();
			}
			else
			{
				mensajesLog("Verifique la Informaciôn de Agenda,Consultorios y Parametros");
			}
		}		
		
		/**
		
		*/
		function iniciarNavegacion()
		{
			//Agrega los eventos de los botones de navegacion				
			for(var i=0;i<mc_navegacion.numChildren; i++)
			{
				if(mc_navegacion.getChildAt(i).name == "btn_derecha")
				{
					mc_navegacion.getChildAt(i).addEventListener(MouseEvent.MOUSE_OUT,pararNavegacion);						
					mc_navegacion.getChildAt(i).addEventListener(MouseEvent.ROLL_OVER,navegacionDerecha);				
					mc_navegacion.getChildAt(i).addEventListener(MouseEvent.CLICK,addIncrementoNav);
				}
				else if(mc_navegacion.getChildAt(i).name == "btn_izquierda")
				{
					mc_navegacion.getChildAt(i).addEventListener(MouseEvent.MOUSE_OUT,pararNavegacion);						
					mc_navegacion.getChildAt(i).addEventListener(MouseEvent.ROLL_OVER,navegacionIzquierda);				
					mc_navegacion.getChildAt(i).addEventListener(MouseEvent.CLICK,addIncrementoNav);						
				}
				else if(mc_navegacion.getChildAt(i).name == "btn_arriba")
				{
					mc_navegacion.getChildAt(i).addEventListener(MouseEvent.MOUSE_OUT,pararNavegacion);						
					mc_navegacion.getChildAt(i).addEventListener(MouseEvent.ROLL_OVER,navegacionArriba);	
					mc_navegacion.getChildAt(i).addEventListener(MouseEvent.CLICK,addIncrementoNav);						
				}
				else if(mc_navegacion.getChildAt(i).name == "btn_abajo")
				{
					mc_navegacion.getChildAt(i).addEventListener(MouseEvent.MOUSE_OUT,pararNavegacion);	
					mc_navegacion.getChildAt(i).addEventListener(MouseEvent.ROLL_OVER,navegacionAbajo);	
					mc_navegacion.getChildAt(i).addEventListener(MouseEvent.CLICK,addIncrementoNav);
				}
				else if(mc_navegacion.getChildAt(i).name == "btn_ext_derecha")
				{
					mc_navegacion.getChildAt(i).addEventListener(MouseEvent.CLICK,navegacionExtDerecha);
				}
				else if(mc_navegacion.getChildAt(i).name == "btn_ext_izquierda")
				{
					mc_navegacion.getChildAt(i).addEventListener(MouseEvent.CLICK,navegacionExtIzquierda);
				}
				else if(mc_navegacion.getChildAt(i).name == "btn_ext_arriba")
				{
					mc_navegacion.getChildAt(i).addEventListener(MouseEvent.CLICK,navegacionExtArriba);
				}
				else if(mc_navegacion.getChildAt(i).name == "btn_ext_abajo")
				{
					mc_navegacion.getChildAt(i).addEventListener(MouseEvent.CLICK,navegacionExtAbajo);
				}
			}

			addEventListener(Event.ENTER_FRAME,navegacion);
		}
		
		function mensajesLog(mensaje:String)
		{
			labelMensajes.text =mensaje;
			labelMensajes.autoSize = TextFieldAutoSize.LEFT;
			labelMensajes.antiAliasType = AntiAliasType.NORMAL; 
			labelMensajes.x = (anchoEscenario /2) - (labelMensajes.width/2)  ;
			labelMensajes.y = (largoEscenario/2) - (labelMensajes.height/2) ;

			this.addChild(labelMensajes);
		}
		
		/**
		Carga los parametros apartir del xml 
		*/
		private function cargarParametros()
		{
			paramAgenda = new ParametrosAgenda();
			
			if(this.xmlParametros != "" )
			{
				var arrayXml:XML = new XML(xmlParametros);				
				for each(var nodo:XML in arrayXml.elements())
				{
					paramAgenda.setIntervalo = nodo.intervalo;
					paramAgenda.setHoraInicio = nodo.hora_inicio;
					paramAgenda.setMinutoInicio = nodo.min_inicio;
					paramAgenda.setHoraFinal = nodo.hora_final;
					paramAgenda.setMinutoFinal = nodo.min_final;
					paramAgenda.setColorIntervalo = "C4DCFF";
					paramAgenda.setColorConsultorio = "0145A9";
				}
			}
		}
	
		
		/**
		Pinta los consultorios
		*/
		private function pintarConsultorios()
		{
			ct_consultorios = new Sprite();
			if(this.xmlConsultorios != "" )
			{
				var posx = posxIniciaConsultorio;
				var arrayXml:XML = new XML(xmlConsultorios);		
				
				var formatConsul:TextFormat = new TextFormat();
				formatConsul.color = 0xFFFFFF;
				formatConsul.size = 15;
				formatConsul.bold = true;
				formatConsul.italic = false;						
				
				for each(var nodo:XML in arrayXml.elements())
				{
					var consultorio:ConsultorioOdo = new ConsultorioOdo(
																		posx,
																		3,
																		(nodo.nombre+"").toLowerCase(),
																		formatConsul,
																		uint("0x"+paramAgenda.getColorConsultorio),
																		anchoConsultorio);
					
					posx += consultorio.width + espacioEntreElementos;
					this.ct_consultorios.addChild(consultorio);
				}
				
				this.addChildAt(ct_consultorios,1);
								
				xmlConsultorios = "";
				
			}			
		}
		
		/**
		Pinta los consultorios
		*/
		private function pintarAgendaXml(xmlAgendaPa:String,isRedibujo:Boolean)
		{
			if(!isRedibujo)
				ct_agendaOdo = new Sprite();
			
			if(xmlAgendaPa != "" )
			{
				var arrayXml:XML = new XML(xmlAgendaPa);		
				
				var formatAge:TextFormat = new TextFormat();
				formatAge.color = 0x0145A9;
				formatAge.size = 15;
				formatAge.bold = true;
				formatAge.font = "Impact";
				formatAge.italic = false;						
				
				var posx:int = 0;
				var posy:int = 0;
				var hora:Date = new Date();
				var horaInicio:Date = new Date();
				var horaFinal:Date = new Date();
				var numInterAnte:int = 0;
				var numInterDesp:int = 0;
				
				for each(var nodo:XML in arrayXml.elements())
				{					
					if(isRedibujo)
					{
						//trace("eliminar >> "+nodo.codigo_pk);
						var anterior:DisplayObject = this.ct_agendaOdo.getChildByName(nodo.codigo_pk);
						this.ct_agendaOdo.removeChild(anterior);						
					}
					
					hora.setHours(Number(paramAgenda.getHoraInicio),
								  Number(paramAgenda.getMinutoInicio));
						
					horaInicio.setHours(Number(nodo.hora_inicio),
									    Number(nodo.min_inicio));
					
					horaFinal.setHours(Number(nodo.hora_final),
									   Number(nodo.min_final));
					
					//captura el numero de intervalos anteriores a la agenda
					numInterAnte = (((horaInicio.getTime() - hora.getTime()) / 1000) / 60) / paramAgenda.getIntervalo;
					
					//captura el numero de intervalos posteriores
					numInterDesp = (((horaFinal.getTime() - hora.getTime()) / 1000) / 60) / paramAgenda.getIntervalo;
					numInterDesp = ((largoIntervalo + espacioEntreElementos) + ((numInterDesp - numInterAnte) * largoIntervalo)); 
				
					if(nodo.pos_consultorio == 1)					
						posx = posxIniciaConsultorio;
					else						
						posx = ((anchoConsultorio + espacioEntreElementos) * (nodo.pos_consultorio - 1)) + posxIniciaConsultorio;

					if(numInterAnte <= 0)
						posy = posyIniciaRango;
					else
						posy = ((largoIntervalo + espacioEntreElementos) * numInterAnte) + posyIniciaRango;
					
					var age:AgendaOdo = new AgendaOdo(
														nodo.codigo_pk,
														posx,
														posy,
														formatAge,
														uint("0x"+nodo.color),
														numInterDesp,
														anchoConsultorio,
														nodo.numcitas);					
					
					age.setPosConsultorio = nodo.pos_consultorio;
					age.setDesProfesional = nodo.profesional;
					age.setDesEspecialidad = nodo.especialidad_prof;
					age.setHoraInicio = nodo.hora_inicio;
					age.setMinInicio = nodo.min_inicio;
					age.setHoraFinal = nodo.hora_final;
					age.setMinFinal = nodo.min_final;
					age.setIndicador = nodo.pos_agenda;
					
					//Recorre las SubAgendas
					for each(var agendaNodo:XML in nodo.Citas.elements())
					{
						var subage:SubAgenda = new SubAgenda(agendaNodo.@hora_ini,
															 agendaNodo.@min_ini,
															 agendaNodo.@hora_fin,
															 agendaNodo.@min_fin
															 );
						
						//Recorre las citas de la agenda
						for each(var citaNodo:XML in agendaNodo.elements())
						{
							var cita:Cita = new Cita(
													 	citaNodo.codigo_pk_cita,
														agendaNodo.@hora_ini,
													 	agendaNodo.@min_ini,
														citaNodo.hora_fin,
														citaNodo.min_fin,
														citaNodo.paciente,
														citaNodo.tipo_iden_paciente,
														citaNodo.num_iden_paciente,
														citaNodo.observaciones,
														citaNodo.estado,
														nodo.profesional,
														citaNodo.duracion,
														citaNodo.pos_cita
													 );
							
							subage.getCitasArray.push(cita);
						}
						
						age.getSubAgendasArray.push(subage);						
					}

					age.pintarSubConsultas(largoIntervalo,espacioEntreElementos,posyIniciaRango,paramAgenda.getIntervalo);
					age.ayuda(formatAge);
					
					this.ct_agendaOdo.addChild(age);
				}
				
				if(!isRedibujo)					
					this.addChildAt(ct_agendaOdo,0);
				else
					xmlSubAgenda = "";
					
				xmlAgenda = "";
			}
		}
		
		/**
		
		*/
		private function setswfpintarsubagenda()
		{
			if(this.xmlSubAgenda != "" )			
				pintarAgendaXml(xmlSubAgenda,true);

		}
		
		/**
		
		*/
		private function pintarRangos()
		{
			if(paramAgenda.getIntervalo > 0)
			{
				ct_rangohoras = new Sprite();
				
				var hora:Date = new Date();
				hora.setHours(Number(paramAgenda.getHoraInicio),
							  Number(paramAgenda.getMinutoInicio));
				
				var horaFinal:Date = new Date();
				horaFinal.setHours(Number(paramAgenda.getHoraFinal),
								   Number(paramAgenda.getMinutoFinal));
								
				var formathora:TextFormat = new TextFormat();
				formathora.color = 0x0145A9;
				formathora.size = 15;
				formathora.bold = true;
				formathora.italic = false;						
				formathora.font = "Arial";
				var posy = posyIniciaRango;
				
				while(hora.getTime() <= horaFinal.getTime())			
				{
					var horaOdo:HorarioOdo = new HorarioOdo(
															5,
															posy,
															hora.getHours(),
															hora.getMinutes(),
															formathora,
															uint("0x"+paramAgenda.getColorIntervalo),
															largoIntervalo
															);

					posy += (horaOdo.height) + espacioEntreElementos;
					this.ct_rangohoras.addChild(horaOdo);
					hora.minutes += paramAgenda.getIntervalo;					
				}
				
				this.addChild(this.ct_rangohoras);
			}
			else
			{
				mensajesLog("El Intervalor debe ser mayor a cero");
			}
		}
		
		/**
		
		*/
		private function iniciarContenedores()
		{
			ct_infoagenda = new Sprite();
			ct_infoagenda.graphics.beginFill(uint("0xC4DCFF"));
			ct_infoagenda.graphics.lineStyle(2,0x011F4B,0.9,true,LineScaleMode.NONE,CapsStyle.ROUND,JointStyle.ROUND);
			ct_infoagenda.graphics.drawRect(0,0,200,100);
			ct_infoagenda.graphics.endFill();
			ct_infoagenda.alpha= 0.9;
			
			ct_infocita = new Sprite();
			ct_infocita.graphics.beginFill(0xFEEEB4);
			ct_infocita.graphics.lineStyle(2,0xFFCC00,0.9,true,LineScaleMode.NONE,CapsStyle.ROUND,JointStyle.ROUND);
			ct_infocita.graphics.drawRect(0,0,200,130);
			ct_infocita.graphics.endFill();
			ct_infocita.alpha= 0.9;
			
			
			//Libera los objetos contenidos dentro de los contenedores
			var l:int = ct_rangohoras.numChildren-1
			var j:int = 0;
			for(j = l; j >= 0;j--){
				ct_rangohoras.removeChildAt(j);
			}
			
			l = ct_consultorios.numChildren-1
			for(j = l; j >= 0;j--){
				ct_consultorios.removeChildAt(j);
			}
			
			l = ct_agendaOdo.numChildren-1
			for(j = l; j >= 0;j--){
				ct_agendaOdo.removeChildAt(j);
			}
		}
		
	
		// Public Methods:
		
		/**		
		
		*/
		public function mostrarInfoComponente(tipo:String,descripcion:Array,posx:int,posy:int)
		{
			var tam = descripcion.length;
			var poslabely:int = 0;
			
			for(var i = 0; i < tam; i++)
			{
				var labelCp:TextField =  new TextField();
				labelCp.defaultTextFormat = formatMensajes;
				labelCp.text = descripcion[i];
				labelCp.autoSize = TextFieldAutoSize.CENTER;
				labelCp.antiAliasType = AntiAliasType.NORMAL; 				
				labelCp.selectable = false;
				labelCp.x = 2;
				labelCp.y = poslabely;
				
				if(tipo == "agenda")		
					ct_infoagenda.addChild(labelCp);
				else if(tipo == "cita")
					ct_infocita.addChild(labelCp);
					
				if(i == 0)
					poslabely +=30;
				else	
					poslabely +=15;
			}			
			
			var resaltador = new Sprite();
			
			if(tipo == "agenda")
			{
				ct_infoagenda.x = posx;
				ct_infoagenda.y = posy;
				
				resaltador.graphics.beginFill(0xF7FEFF);
				resaltador.graphics.lineStyle(0,0x011F4B,1);
				resaltador.graphics.drawRect(1,0,199,20);
				resaltador.alpha = 0.9;
				resaltador.graphics.endFill();
				
				this.ct_infoagenda.addChildAt(resaltador,0);
				this.ct_agendaOdo.addChild(ct_infoagenda);				
			}
			else if(tipo == "cita")
			{
				ct_infocita.x = posx;
				ct_infocita.y = posy;
				
				resaltador.graphics.beginFill(0xFEE689);
				resaltador.graphics.lineStyle(0,0xFFCC00,1);
				resaltador.graphics.drawRect(0,0,200,20);
				resaltador.alpha = 0.9;
				resaltador.graphics.endFill();
				
				this.ct_infoagenda.addChildAt(resaltador,0);
				
				this.ct_infocita.addChildAt(resaltador,0);
				this.ct_agendaOdo.addChild(ct_infocita);
			}
		}
		
		/**
		
		*/
		public function ocultarInfo(tipo:String)
		{
			var tmp:Sprite = new Sprite();
			
			if(tipo == "agenda")
				tmp = ct_infoagenda;
			else if(tipo == "cita")
				tmp = ct_infocita;
			
			var l:int = tmp.numChildren-1
			for(var j = l; j >= 0;j--){
				tmp.removeChildAt(j);
			}
			
			this.ct_agendaOdo.removeChild(tmp);
		}
				
		/**
		*/
		public function reset()
		{
			xmlAgenda = "";
			xmlConsultorios = "";
			xmlParametros = "";
			paramAgenda = new ParametrosAgenda()
		}
		
		/**
		actualiza el xml de agenda
		*/
		public function setswfxmlagenda(valor:String)
		{
			this.xmlAgenda = valor;
		}
		
		/**
		actualiza el xml de sub agenda
		*/
		public function setswfxmlcitagenda(valor:String)
		{
			this.xmlSubAgenda = valor;
		}
		
		/**
		actualiza el xml de Consultorios
		*/
		public function setswfxmlconsultorios(valor:String)
		{
			this.xmlConsultorios = valor;
		}
		
		/**
		actualiza el xml de parametros
		*/
		public function setswfxmlparametros(valor:String)
		{
			this.xmlParametros = valor;
		}
		
		/**
		
		*/
		private function navegacionDerecha(e:MouseEvent)
		{
			horientacionNav = Constantes.codigoNavegacionDerecha;
		}
		
		/**
		
		*/
		private function navegacionArriba(e:MouseEvent)
		{
			horientacionNav = Constantes.codigoNavegacionArriba;
		}
		
		/**
		
		*/
		private function navegacionAbajo(e:MouseEvent)
		{
			horientacionNav = Constantes.codigoNavegacionAbajo;			
		}
		
		/**
		
		*/
		private function navegacionIzquierda(e:MouseEvent)
		{
			horientacionNav = Constantes.codigoNavegacionIzquierda;			
		}
		
		/**		
		*/
		private function navegacionExtDerecha(e:MouseEvent)
		{
			ct_consultorios.x = - (ct_consultorios.width - 200 );
			ct_agendaOdo.x = - (ct_consultorios.width - 200);
		}
		
		/**		
		*/
		private function navegacionExtIzquierda(e:MouseEvent)
		{			
			ct_consultorios.x = 0;
			ct_agendaOdo.x = 0;		
		}
		
		/**		
		*/
		private function navegacionExtArriba(e:MouseEvent)
		{			
			ct_rangohoras.y = 0;
			ct_agendaOdo.y = 0;
		}
		
		/**		
		*/
		private function navegacionExtAbajo(e:MouseEvent)
		{
			ct_rangohoras.y = - (ct_rangohoras.height - 500);
			ct_agendaOdo.y  = - (ct_rangohoras.height - 500);
		}
		
		/**
		
		*/
		private function pararNavegacion(e:MouseEvent)
		{
			horientacionNav = 0;
			incremetoNav = 10;			
		}
		
		/**
		*/
		private function addIncrementoNav(e:MouseEvent)
		{
			incremetoNav += 5;
		}
		
		/**
		
		*/
		private function navegacion(e:Event)
		{
			if(horientacionNav == Constantes.codigoNavegacionArriba)
			{
				if(ct_rangohoras.y<0)
				{
					ct_rangohoras.y += incremetoNav;
					ct_agendaOdo.y += incremetoNav;					
				}
			}
			else if(horientacionNav == Constantes.codigoNavegacionAbajo)
			{
				if(((ct_rangohoras.height - 500) - Math.abs(ct_rangohoras.y)) > 0 )
				{
					ct_rangohoras.y -= incremetoNav;
					ct_agendaOdo.y -= incremetoNav;			
				}
			}
			else if(horientacionNav == Constantes.codigoNavegacionDerecha)
			{
				if(((ct_consultorios.width - 200) - Math.abs(ct_consultorios.x)) > 0 )
				{
					ct_consultorios.x -= incremetoNav;
					ct_agendaOdo.x -= incremetoNav;			
				}
			}
			else if(horientacionNav == Constantes.codigoNavegacionIzquierda)
			{
				if(ct_consultorios.x < 0)
				{
					ct_consultorios.x += incremetoNav;
					ct_agendaOdo.x += incremetoNav;			
				}			
			}
			
			//evalua la posicion del mouse para mostrar o no los botones de navegación			
			if(e.target.mouseY > 500 && !menuNavegacionActivo)
			{
				menuNavegacionActivo = true;
				mc_navegacion.x = 426;
				mc_navegacion.y = 549;
				addChild(mc_navegacion);
			}
			else if(e.target.mouseY < 500 && menuNavegacionActivo)
			{
				menuNavegacionActivo = false;
				this.removeChild(mc_navegacion);
			}
		}
	}
}