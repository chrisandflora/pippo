/*
 * Copyright (C) 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ro.pippo.librato;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.pippo.core.PippoSettings;
import ro.pippo.metrics.MetricsReporter;

import com.codahale.metrics.MetricRegistry;
import com.librato.metrics.LibratoReporter;

/**
 * Integration of Pippo Metrics with Librato.
 *
 * @author James Moger
 *
 */
public class Reporter implements MetricsReporter {

	private final Logger log = LoggerFactory.getLogger(Reporter.class);

	@Override
	public void start(PippoSettings settings, MetricRegistry metricRegistry) {
		if (settings.getBoolean("metrics.librato.enabled", false)) {

			final String hostname = settings.getLocalHostname();
			final String username = settings.getRequiredString("metrics.librato.username");
			final String apiKey = settings.getRequiredString("metrics.librato.apikey");
			final long period = settings.getDurationInSeconds("metrics.librato.period", 60);

			LibratoReporter.enable(LibratoReporter.builder(metricRegistry, username, apiKey, hostname), period,
					TimeUnit.SECONDS);

			log.info("Started Librato Metrics reporter for '{}', updating every {} seconds", hostname, period);

		} else {
			log.debug("Librato Metrics reporter is disabled");
		}
	}

	@Override
	public void close() throws IOException {
	}
}
