/* Copyright (C) 2016 Julian Andres Klode <jak@jak-linux.org>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package org.jak_linux.dns66;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.JsonReader;
import android.util.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuration class. This is serialized as JSON using read() and write() methods.
 *
 * @author Julian Andres Klode
 */
public class Configuration extends BaseObservable {
    private static final int VERSION = 1;
    private boolean autoStart = false;
    private Hosts hosts = new Hosts();
    private DnsServers dnsServers = new DnsServers();

    private static Hosts readHosts(JsonReader reader) throws IOException {
        Hosts hosts = new Hosts();
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "enabled":
                    hosts.setEnabled(reader.nextBoolean());
                    break;
                case "items":
                    hosts.setItems(readItemList(reader));
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        return hosts;
    }

    private static DnsServers readDnsServers(JsonReader reader) throws IOException {
        DnsServers servers = new DnsServers();
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "enabled":
                    servers.setEnabled(reader.nextBoolean());
                    break;
                case "items":
                    servers.setItems(readItemList(reader));
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        return servers;
    }

    private static List<Item> readItemList(JsonReader reader) throws IOException {
        reader.beginArray();
        List<Item> list = new ArrayList<Item>();
        while (reader.hasNext())
            list.add(readItem(reader));

        reader.endArray();
        return list;
    }

    private static Item readItem(JsonReader reader) throws IOException {
        Item item = new Item();
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "title":
                    item.setTitle(reader.nextString());
                    break;
                case "location":
                    item.setLocation(reader.nextString());
                    break;
                case "state":
                    item.setState(reader.nextInt());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        return item;
    }

    private static void writeHosts(JsonWriter writer, Hosts h) throws IOException {
        writer.beginObject();
        writer.name("enabled").value(h.isEnabled());
        writer.name("items");
        writeItemList(writer, h.getItems());
        writer.endObject();
    }

    private static void writeDnsServers(JsonWriter writer, DnsServers s) throws IOException {
        writer.beginObject();
        writer.name("enabled").value(s.isEnabled());
        writer.name("items");
        writeItemList(writer, s.getItems());
        writer.endObject();
    }

    private static void writeItemList(JsonWriter writer, List<Item> items) throws IOException {
        writer.beginArray();
        for (Item i : items) {
            writeItem(writer, i);
        }
        writer.endArray();
    }

    private static void writeItem(JsonWriter writer, Item i) throws IOException {
        writer.beginObject();
        writer.name("title").value(i.getTitle());
        writer.name("location").value(i.getLocation());
        writer.name("state").value(i.getState());
        writer.endObject();
    }

    public void write(JsonWriter writer) throws IOException {
        writer.beginObject();
        writer.name("version").value(VERSION);
        writer.name("autoStart").value(isAutoStart());
        writer.name("hosts");
        writeHosts(writer, getHosts());
        writer.name("dnsServers");
        writeDnsServers(writer, getDnsServers());
        writer.endObject();
    }

    public void read(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "version":
                    if (reader.nextInt() > VERSION) {
                        throw new RuntimeException("Incompatible format");
                    }
                    break;
                case "autoStart":
                    setAutoStart(reader.nextBoolean());
                    break;
                case "hosts":
                    setHosts(readHosts(reader));
                    break;
                case "dnsServers":
                    setDnsServers(readDnsServers(reader));
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
    }

    @Bindable
    public boolean isAutoStart() {
        return autoStart;
    }

    public void setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
        notifyPropertyChanged(BR.autoStart);
    }

    public Hosts getHosts() {
        return hosts;
    }

    public void setHosts(Hosts hosts) {
        this.hosts = hosts;
    }

    public DnsServers getDnsServers() {
        return dnsServers;
    }

    public void setDnsServers(DnsServers dnsServers) {
        this.dnsServers = dnsServers;
    }

    public static class Item extends BaseObservable {
        public static final int STATE_IGNORE = 2;
        public static final int STATE_DENY = 0;
        public static final int STATE_ALLOW = 1;
        private String title;
        private String location;
        private int state;

        @Bindable
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
            notifyPropertyChanged(BR.title);
        }

        @Bindable
        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
            notifyPropertyChanged(BR.location);
        }

        @Bindable
        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
            notifyPropertyChanged(BR.state);
        }
    }

    public static class Hosts extends BaseObservable {
        private boolean enabled;
        private List<Item> items = new ArrayList<>();

        @Bindable
        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
            notifyPropertyChanged(BR.enabled);
        }

        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }
    }

    public static class DnsServers extends BaseObservable {
        private boolean enabled;
        private List<Item> items = new ArrayList<>();

        @Bindable
        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
            notifyPropertyChanged(BR.enabled);
        }

        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }
    }
}
