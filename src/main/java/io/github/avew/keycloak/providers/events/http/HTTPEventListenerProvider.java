/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.avew.keycloak.providers.events.http;

import com.google.gson.Gson;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;

import java.util.Set;
import java.lang.Exception;

import okhttp3.*;

import java.io.IOException;

/**
 * @author <a href="mailto:jessy.lenne@stadline.com">Jessy Lenne</a>
 */
public class HTTPEventListenerProvider implements EventListenerProvider {
    private final OkHttpClient httpClient = new OkHttpClient();
    private Set<EventType> excludedEvents;
    private Set<OperationType> excludedAdminOperations;
    private String serverUri;
    private String username;
    private String password;
    public static final String publisherId = "keycloak";
    public String TOPIC;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public HTTPEventListenerProvider(Set<EventType> excludedEvents, Set<OperationType> excludedAdminOperations, String serverUri, String username, String password, String topic) {
        this.excludedEvents = excludedEvents;
        this.excludedAdminOperations = excludedAdminOperations;
        this.serverUri = serverUri;
        this.username = username;
        this.password = password;
        this.TOPIC = topic;
    }

    @Override
    public void onEvent(Event event) {
        if (this.excludedEvents != null && this.excludedEvents.contains(event.getType())) {
            return;
        }
        final String stringEvent = this.toString(event);
        try {
            RequestBody(stringEvent);
        } catch (Exception e) {
            System.out.println("UH OH!! " + e);
            e.printStackTrace();
        }
    }

    @Override
    public void onEvent(AdminEvent event, boolean includeRepresentation) {
        if (this.excludedAdminOperations != null && this.excludedAdminOperations.contains(event.getOperationType())) {
            return;
        }
        final String stringEvent = this.toString(event);
        try {
            RequestBody(stringEvent);
        } catch (Exception e) {
            System.out.println("UH OH!! " + e);
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    private void RequestBody(String stringEvent) throws IOException {
        RequestBody body = RequestBody.create(HTTPEventListenerProvider.JSON, stringEvent);
        Request request = new Request.Builder().url(this.serverUri).post(body).build();
        Response response = this.httpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        System.out.println(response.body().string());
    }


    private String toString(final Event event) {
        return new Gson().toJson(event);
    }

    private String toString(final AdminEvent adminEvent) {
        return new Gson().toJson(adminEvent);
    }

    @Override
    public void close() {
    }

}
