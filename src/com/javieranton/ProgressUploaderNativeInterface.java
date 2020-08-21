/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javieranton;

import com.codename1.system.NativeInterface;

/**
 *
 * @author user
 */
public interface ProgressUploaderNativeInterface extends NativeInterface{
    public void PostMultipart(String url,String fileContent);
}
