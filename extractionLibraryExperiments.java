/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bibliotecaextração;

/**
 *
 * @author alessandro
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author alessandromurtabaldi
 */
public class extractionLibraryExperiments {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String leitura = null;

        try {
            // TODO code application logic here
            leitura = readFile("points.txt"); //insert points from another extractor here

        } catch (IOException ex) {
            Logger.getLogger(extractionLibraryExperiments.class.getName()).log(Level.SEVERE, null, ex);
        }
        String[] leituraslat = leitura.split("lat:");
        String[] leituraslon = leitura.split("lon:");
        List<Double> coordlat = new ArrayList<Double>();
        List<Double> coordlon = new ArrayList<Double>();
        List<Double> coordlatat = new ArrayList<Double>();
        List<Double> coordlonat = new ArrayList<Double>();
        double latmedia = 0.0;
        double lonmedia = 0.0;
        int totalex = 0;
        for (int cont = 1; cont < leituraslat.length; cont++) {
            String atual[] = leituraslat[cont].split(",");
            coordlatat.add(Double.parseDouble(atual[0]));
            latmedia = latmedia + Double.parseDouble(atual[0]);
        }
        for (int cont = 1; cont < leituraslon.length; cont++) {
            String atual2[] = leituraslon[cont].split("\n");
            coordlonat.add(Double.parseDouble(atual2[0]));
            lonmedia = lonmedia + Double.parseDouble(atual2[0]);
        }
        latmedia = latmedia / coordlatat.size();
        lonmedia = lonmedia / coordlonat.size();

        File javascener = new File("javascene.txt");//java / scene output points
        FileWriter javascene = new FileWriter(javascener);

        File groover = new File("groove.txt");//groove output points
        FileWriter groove = new FileWriter(groover);

        groove.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<gxl xmlns=\"http://www.gupro.de/GXL/gxl-1.0.dtd\">\n"
                + "    <graph role=\"graph\" edgeids=\"false\" edgemode=\"directed\" id=\"start\">\n"
                + "        <attr name=\"$version\">\n"
                + "            <string>curly</string>\n"
                + "        </attr>\n");

        File repaster = new File("repast.txt");//repast output points
        FileWriter repast = new FileWriter(repaster);
        repast.write("clearAll()\n");

        for (int casa = 0; casa < coordlatat.size(); casa++) {
            double distancialat = distance(coordlatat.get(casa), 0, latmedia, 0, "K");
            double distancialon = distance(0, coordlonat.get(casa), 0, lonmedia, "K");
            distancialat = distancialat * 1000;
            distancialon = distancialon * 1000;
            javascene.write("C" + String.valueOf(casa + 1) + ",");

            groove.write("        <node id=\"n");
            groove.write(String.valueOf(casa + coordlat.size()));
            groove.write("\">\n"
                    + "            <attr name=\"layout\">\n"
                    + "                <string>" + 20 + " " + 30 + " 71 32</string>\n"
                    + "            </attr>\n"
                    + "        </node>\n");

            if (coordlatat.get(casa) < latmedia) {
                distancialat = distancialat * -1;
            }
            if (coordlonat.get(casa) < lonmedia) {
                distancialon = distancialon * -1;
            }
            if (casa % 22 == 0 && casa > 0) {
                repast.write("setDefaultShape(House, \"house\")\n");
                repast.write("createHouses(1){\n"
                        + "setxy(" + distancialat + "," + distancialon + ")\n"
                        + "setTrap(true)\n"
                        + "}\n");
            } else {
                repast.write("setDefaultShape(House, \"house\")\n");
                repast.write("createHouses(1){\n"
                        + "setxy(" + distancialat + "," + distancialon + ")\n"
                        + "setActivefocus(true)\n"
                        + "}\n");
            }
            if (casa < 264) {
                repast.write("setDefaultShape(Mosquito, \"bug\")\n");
                repast.write("createMosquitos(1){\n"
                        + "setxy(" + distancialat + "," + distancialon + ")\n" + "size=2\n"
                        + "}\n");
                repast.write("setDefaultShape(Mosquito, \"bug\")\n");
                repast.write("createMosquitos(1){\n"
                        + "setxy(" + distancialat + "," + distancialon + ")\n" + "size=2\n"
                        + "}\n");
                repast.write("setDefaultShape(Mosquito, \"bug\")\n");
                repast.write("createMosquitos(1){\n"
                        + "setxy(" + distancialat + "," + distancialon + ")\n" + "size=2\n"
                        + "}\n");
            } else {
                repast.write("setDefaultShape(Mosquito, \"bug\")\n");
                repast.write("createMosquitos(1){\n"
                        + "setxy(" + distancialat + "," + distancialon + ")\n" + "size=2\n"
                        + "}\n");
                repast.write("setDefaultShape(Mosquito, \"bug\")\n");
                repast.write("createMosquitos(1){\n"
                        + "setxy(" + distancialat + "," + distancialon + ")\n" + "size=2\n"
                        + "}\n");

            }
        }
        javascene.write("\n");
        javascene.write("\n");
        javascene.write("\n");
        for (int casa = 0; casa < coordlatat.size(); casa++) {
            for (int casa2 = casa + 1; casa2 < coordlatat.size(); casa2++) {
                double distancia = distance(coordlatat.get(casa), coordlonat.get(casa), coordlatat.get(casa2), coordlonat.get(casa2), "K");
                distancia = distancia * 1000;
                if (distancia < 100) {
                    javascene.write("C" + String.valueOf(casa + 1)
                            + "-" + "C" + String.valueOf(casa2 + 1)
                            + ",");
                }
            }
        }
        javascene.write("\n");
        javascene.write("\n");
        javascene.write("\n");
        for (int casa = 0; casa < coordlatat.size(); casa++) {
            if (casa % 22 == 0) {
                javascene.write("C" + String.valueOf(casa + 1) + ",");//trap
            }
        }
        javascene.write("\n");
        javascene.write("\n");
        javascene.write("\n");

        for (int casa = 0; casa < coordlatat.size(); casa++) {
            if (casa % 22 != 0) {
                javascene.write("C" + String.valueOf(casa + 1) + ",");//focus
            }
        }

        javascene.write("\n");
        javascene.write("\n");
        javascene.write("\n");
        for (int casa = 0; casa < coordlatat.size(); casa++) {
            if (casa < 264) {//3 mosquitos
                javascene.write("C" + String.valueOf(casa + 1) + ",");
                javascene.write("C" + String.valueOf(casa + 1) + ",");
                javascene.write("C" + String.valueOf(casa + 1) + ",");
            } else {//2 mosquitos
                javascene.write("C" + String.valueOf(casa + 1) + ",");
                javascene.write("C" + String.valueOf(casa + 1) + ",");
            }
        }

        for (int cont = 0; cont < coordlatat.size(); cont++) {
            groove.write("        <edge from=\"n" + String.valueOf(cont)
                    + "\" to=\"n" + String.valueOf(cont)
                    + "\">\n");
            groove.write("            <attr name=\"label\">\n"
                    + "                <string>type:casa</string>\n"
                    + "            </attr>\n"
                    + "        </edge>\n");
        }

        for (int cont = coordlatat.size(); cont < coordlatat.size() * 2; cont++) {
            groove.write("        <edge from=\"n" + String.valueOf(cont)
                    + "\" to=\"n" + String.valueOf(cont)
                    + "\">\n");
            groove.write("            <attr name=\"label\">\n"
                    + "                <string>bool:true</string>\n"
                    + "            </attr>\n"
                    + "        </edge>\n");
        }
        for (int cont = 0; cont < coordlatat.size(); cont++) {
            groove.write("        <edge from=\"n" + String.valueOf(cont)
                    + "\" to=\"n" + String.valueOf((cont + coordlatat.size()))
                    + "\">\n");
            groove.write("            <attr name=\"label\">\n"
                    + "                <string>flag:foco</string>\n"
                    + "            </attr>\n"
                    + "        </edge>\n");
        }

        for (int cont = 0; cont < coordlat.size(); cont++) {
            for (int cont2 = cont + 1; cont2 < coordlat.size(); cont2++) {
                double calculo = distance(coordlatat.get(cont), coordlonat.get(cont), coordlatat.get(cont2), coordlonat.get(cont2), "K");
                if (calculo < 100) {
                    groove.write("        <edge from=\"n" + String.valueOf(cont)
                            + "\" to=\"n" + String.valueOf(cont2)
                            + "\">\n");
                    groove.write("            <attr name=\"label\">\n"
                            + "                <string>voar</string>\n"
                            + "            </attr>\n"
                            + "        </edge>\n");
                    groove.write("        <edge from=\"n" + String.valueOf(cont2)
                            + "\" to=\"n" + String.valueOf(cont)
                            + "\">\n");
                    groove.write("            <attr name=\"label\">\n"
                            + "                <string>voar</string>\n"
                            + "            </attr>\n"
                            + "        </edge>\n");
                }
            }
        }
        groove.write("    </graph>\n"
                + "</gxl>");
        groove.flush();

        repast.flush();
        javascene.flush();
    }

    public static String readFile(String fileName) throws IOException {//function to read file
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }


    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {//function to calculate distance of points
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = dist * 1.609344;
        } else if (unit == "N") {
            dist = dist * 0.8684;
        }

        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

}
