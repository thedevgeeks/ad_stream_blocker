/* Copyright (C) 2016 Julian Andres Klode <jak@jak-linux.org>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package org.jak_linux.dns66.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jak_linux.dns66.MainActivity;
import org.jak_linux.dns66.databinding.FragmentStartBinding;

public class StartFragment extends Fragment {
    public StartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentStartBinding binding = FragmentStartBinding.inflate(inflater, container, false);
        binding.setConfig(MainActivity.config);
        return binding.getRoot();
    }
}
