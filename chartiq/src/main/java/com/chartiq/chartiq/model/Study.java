package com.chartiq.chartiq.model;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by sobolev on 2/17/17.
 */

public class Study implements Serializable{
    public Map<String, Object> attributes;
    public Double centerline;
    public Boolean customRemoval;
    public Boolean deferUpdate;
    public String display;
    public Map<String, Object> inputs;
    public String name;
    public Map<String, Object> outputs;
    public Boolean overlay;
    public Map<String, Object> parameters;
    public String range;
    public String shortName;
    public Boolean underlay;
    public Map<String, Object> yAxis;
    public String type;

    @Override
    public boolean equals(Object obj) {
        boolean equality = super.equals(obj);
        if(!equality && obj instanceof Study){
            return this.shortName != null && this.shortName.equals(((Study) obj).shortName);
        }
        return equality;
    }
}
