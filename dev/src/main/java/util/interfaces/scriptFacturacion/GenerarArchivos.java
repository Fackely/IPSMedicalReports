// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GenerarArchivos.java

package util.interfaces.scriptFacturacion;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.Date;
import javax.swing.*;

// Referenced classes of package generarArchivosFacCar:
//            ConsultaFacturacion

public class GenerarArchivos
{

    public GenerarArchivos()
    {
        frameSwing = new JFrame("SwingApplication");
        panelContainer = new JPanel();
        fechaInicialLabel = new JLabel("Fecha Inicial (yyyy-mm-dd): ");
        fechaInicialText = new JTextField();
        fechaFinalLabel = new JLabel("Fecha Final (yyyy-mm-dd): ");
        fechaFinalText = new JTextField();
        tipoConsultaLabel = new JLabel("Tipo Consulta: ");
        tipoConsulta = new JComboBox();
        rutaLabel = new JLabel("Ruta para generar los Archivos: ");
        rutaText = new JTextField();
        objectiveLabel = new JLabel(labelPrefix);
        generarArchivos = new JButton("Generar!");
        xJDBCDriverLabel = new JLabel("JDBC Driver: ");
        xJDBCDriver = new JComboBox();
        xDataBaseURLLabel = new JLabel("Data Base Url: ");
        xDataBaseURLText = new JTextField();
        xDataBaseURLLabelExample = new JLabel("Ejemplos:");
        xDataBaseURLLabelExample0 = new JLabel("jdbc:postgresql://<host>:<port>/<database>");
        xDataBaseURLLabelExample1 = new JLabel("jdbc:postgresql://localhost:5432/axioma ");
        xDataBaseURLLabelExample2 = new JLabel("jdbc:oracle:thin:@descartes:1521:axioma");
        xUserIDLabel = new JLabel("User Id: ");
        xUserIdText = new JTextField();
        xPasswordBDLabel = new JLabel("Password: ");
        xPasswordBD = new JPasswordField();
    }

    public static void main(String args[])
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }
        catch(Exception exception) { }
        GenerarArchivos ga = new GenerarArchivos();
        ga.createFrame();
    }

    public void createFrame()
    {
        int widthFrame = 470;
        int heightFrame = 480;
        createPanelContainer();
        frameSwing.getContentPane().add(panelContainer, "Center");
        frameSwing.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
        frameSwing.pack();
        frameSwing.setSize(widthFrame, heightFrame);
        frameSwing.setTitle("::: GENERAR ARCHIVOS :::");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frameSwing.setLocation(screenSize.width / 2 - widthFrame / 2, screenSize.height / 2 - heightFrame / 2);
        frameSwing.setVisible(true);
    }

    public void createPanelContainer()
    {
        int top = 10;
        int left = 10;
        int bottom = 10;
        int right = 10;
        int cornerY = 20;
        actionListenerButton();
        panelContainer.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
        panelContainer.setLayout(null);
        xJDBCDriver.addItem("org.postgresql.Driver");
        xJDBCDriver.addItem("oracle.jdbc.driver.OracleDriver");
        xJDBCDriver.addItem("oracle.jdbc.OracleDriver");
        tipoConsulta.addItem("HO");
        tipoConsulta.addItem("AM");
        objectiveLabel.setBounds(120, cornerY, 250, 25);
        panelContainer.add(objectiveLabel);
        cornerY += 40;
        cornerY = addToPanel(xJDBCDriverLabel, xJDBCDriver, cornerY);
        cornerY = addToPanel(xDataBaseURLLabel, xDataBaseURLText, cornerY);
        cornerY = addLabelExample(xDataBaseURLLabelExample, cornerY);
        cornerY = addLabelExample(xDataBaseURLLabelExample0, cornerY);
        cornerY = addLabelExample(xDataBaseURLLabelExample1, cornerY);
        cornerY = addLabelExample(xDataBaseURLLabelExample2, cornerY);
        cornerY = addToPanel(xUserIDLabel, xUserIdText, cornerY);
        cornerY = addToPanel(xPasswordBDLabel, xPasswordBD, cornerY);
        cornerY += 40;
        cornerY = addToPanel(tipoConsultaLabel, tipoConsulta, cornerY);
        cornerY = addToPanel(fechaInicialLabel, fechaInicialText, cornerY);
        cornerY = addToPanel(fechaFinalLabel, fechaFinalText, cornerY);
        cornerY = addToPanel(rutaLabel, rutaText, cornerY);
        cornerY += 10;
        generarArchivos.setBounds(150, cornerY, 200, 25);
        panelContainer.add(generarArchivos);
    }

    public int addLabelExample(JLabel labelExample, int cornerY)
    {
        int cornerX = 140;
        int height = 20;
        int width = 400;
        labelExample.setBounds(cornerX, cornerY, width, height);
        panelContainer.add(labelExample);
        return cornerY += height;
    }

    public int addToPanel(Component componentLabel, Component componentRead, int cornerY)
    {
        int cornerXLabels = 20;
        int cornerXComponentsRead = 240;
        int heightComponent = 20;
        int widthLabels = 230;
        int widthComponentsRead = 200;
        componentLabel.setBounds(cornerXLabels, cornerY, widthLabels, heightComponent);
        panelContainer.add(componentLabel);
        componentRead.setBounds(cornerXComponentsRead, cornerY, widthComponentsRead, heightComponent);
        panelContainer.add(componentRead);
        return cornerY += heightComponent;
    }

    private void actionListenerButton()
    {
        generarArchivos.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if(flowGenerate())
                    callDialog();
            }
        });
    }

    public boolean flowGenerate()
    {
        boolean contieneErrores = false;
        String tipo = "";
        String fInicial = "";
        String fFinal = "";
        String ruta = "";
        tipo = tipoConsulta.getSelectedItem().toString();
        fInicial = fechaInicialText.getText();
        fFinal = fechaFinalText.getText();
        ruta = rutaText.getText();
        String xJDBCDriver = this.xJDBCDriver.getSelectedItem().toString();
        String xDatabaseURL = xDataBaseURLText.getText();
        String xUser = xUserIdText.getText();
        String xPassword = "";
        if(xPasswordBD.getPassword() != null)
        {
            for(int w = 0; w < xPasswordBD.getPassword().length; w++)
                xPassword = (new StringBuilder(String.valueOf(xPassword))).append(xPasswordBD.getPassword()[w]).toString();

        }
        if(xJDBCDriver.trim().equals(""))
        {
            contieneErrores = true;
            JOptionPane.showMessageDialog(frameSwing, "El JDBC Driver es requerido");
        }
        if(xDatabaseURL.trim().equals(""))
        {
            contieneErrores = true;
            JOptionPane.showMessageDialog(frameSwing, "El URL Driver es requerido");
        }
        if(xUser.trim().equals(""))
        {
            contieneErrores = true;
            JOptionPane.showMessageDialog(frameSwing, "El User Id es requerido");
        }
        if(xPassword.trim().equals(""))
        {
            contieneErrores = true;
            JOptionPane.showMessageDialog(frameSwing, "El Password es requerido");
        }
        if(!fInicial.trim().equals(""))
        {
            try
            {
                Date.valueOf(fInicial);
            }
            catch(Exception e)
            {
                contieneErrores = true;
                JOptionPane.showMessageDialog(frameSwing, "La Fecha debe estar en formato yyyy-mm-dd");
            }
        } else
        {
            contieneErrores = true;
            JOptionPane.showMessageDialog(frameSwing, "La fecha inicial es requerida");
        }
        if(!fFinal.trim().equals(""))
        {
            try
            {
                Date.valueOf(fFinal);
            }
            catch(Exception e)
            {
                contieneErrores = true;
                JOptionPane.showMessageDialog(frameSwing, "La Fecha debe estar en formato yyyy-mm-dd");
            }
        } else
        {
            contieneErrores = true;
            JOptionPane.showMessageDialog(frameSwing, "La fecha Final es requerida");
        }
        if(ruta.trim().equals(""))
        {
            contieneErrores = true;
            JOptionPane.showMessageDialog(frameSwing, "La Ruta de generacion es requerida");
        } else
        if(!contieneErrores)
        {
            File directorio = new File(ruta);
            if(!directorio.isDirectory() && !directorio.exists() && !directorio.mkdirs())
            {
                contieneErrores = true;
                JOptionPane.showMessageDialog(frameSwing, "Error Creando el directorio.");
            }
        }
        if(!contieneErrores)
        {
            ConsultaFacturacion.generarArchivos(tipo, fInicial, fFinal, ruta, xJDBCDriver, xDatabaseURL, xUser, xPassword);
            return true;
        } else
        {
            return false;
        }
    }

    public void callDialog()
    {
        JOptionPane.showMessageDialog(frameSwing, "Archivos generados Exitosamente.");
        System.exit(0);
    }

    private JFrame frameSwing;
    private JPanel panelContainer;
    private JLabel fechaInicialLabel;
    private JTextField fechaInicialText;
    private JLabel fechaFinalLabel;
    private JTextField fechaFinalText;
    private JLabel tipoConsultaLabel;
    private JComboBox tipoConsulta;
    private JLabel rutaLabel;
    private JTextField rutaText;
    private JLabel objectiveLabel;
    private static String labelPrefix = "GENERAR ARCHIVOS";
    private JButton generarArchivos;
    private JLabel xJDBCDriverLabel;
    private JComboBox xJDBCDriver;
    private JLabel xDataBaseURLLabel;
    private JTextField xDataBaseURLText;
    private JLabel xDataBaseURLLabelExample;
    private JLabel xDataBaseURLLabelExample0;
    private JLabel xDataBaseURLLabelExample1;
    private JLabel xDataBaseURLLabelExample2;
    private JLabel xUserIDLabel;
    private JTextField xUserIdText;
    private JLabel xPasswordBDLabel;
    private JPasswordField xPasswordBD;

}
