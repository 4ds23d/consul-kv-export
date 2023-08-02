package org.example.consul;


import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
class KVValue {
    @SerializedName("CreateIndex")
    private Integer index;
    @SerializedName("ModifyIndex")
    private Integer modifyIndex;
    @SerializedName("LockIndex")
    private Integer lockIndex;
    @SerializedName("Flags")
    private Integer flags;
    @SerializedName("Key")
    private String key;
    @SerializedName("Value")
    private String value;
}
