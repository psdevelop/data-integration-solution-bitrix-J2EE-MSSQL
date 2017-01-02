/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

/**
 *
 * @author Poltarokov SP
 */
public class Tools {
    public static int parseInt(String parse_str, int default_value) {
        try {
            return Integer.parseInt(parse_str, 10);
        } catch(Exception e)    {
            return default_value;
        }
    }
    
    public static double parseDouble(String parse_str, double default_value) {
        try {
            return Double.parseDouble(parse_str);
        } catch(Exception e)    {
            return default_value;
        }
    }
}
