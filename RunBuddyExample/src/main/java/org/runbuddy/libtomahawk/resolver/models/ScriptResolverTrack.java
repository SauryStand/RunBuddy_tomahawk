/* == This file is part of Tomahawk Player - <http://tomahawk-player.org> ===
 *
 *   Copyright 2015, Enno Gottschalk <mrmaffen@googlemail.com>
 *
 *   Tomahawk is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Tomahawk is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Tomahawk. If not, see <http://www.gnu.org/licenses/>.
 */
package org.runbuddy.libtomahawk.resolver.models;

public class ScriptResolverTrack {

    public String track;

    public String album;

    public String imagePath;

    public String artist;

    public String artistDisambiguation;

    public String albumArtist;

    public String albumArtistDisambiguation;

    public String url;

    public float duration;

    public String linkUrl;

    public int albumpos;

    public long lastModified;

    public ScriptResolverTrack() {
    }
}
