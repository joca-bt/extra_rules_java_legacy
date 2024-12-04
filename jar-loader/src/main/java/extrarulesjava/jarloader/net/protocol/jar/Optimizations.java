/*
 * Copyright 2012-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package extrarulesjava.jarloader.net.protocol.jar;

/**
 * {@link ThreadLocal} state for {@link Handler} optimizations.
 *
 * @author Phillip Webb
 */
final class Optimizations {

	private static final ThreadLocal<Boolean> status = new ThreadLocal<>();

	private Optimizations() {
	}

	static void enable(boolean readContents) {
		status.set(readContents);
	}

	static void disable() {
		status.remove();
	}

	static boolean isEnabled() {
		return status.get() != null;
	}

	static boolean isEnabled(boolean readContents) {
		return Boolean.valueOf(readContents).equals(status.get());
	}

}