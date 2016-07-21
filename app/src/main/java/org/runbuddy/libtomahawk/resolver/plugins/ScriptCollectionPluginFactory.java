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
package org.runbuddy.libtomahawk.resolver.plugins;

import org.runbuddy.libtomahawk.collection.CollectionManager;
import org.runbuddy.libtomahawk.collection.ScriptResolverCollection;
import org.runbuddy.libtomahawk.resolver.ScriptAccount;
import org.runbuddy.libtomahawk.resolver.ScriptObject;

public class ScriptCollectionPluginFactory extends ScriptPluginFactory<ScriptResolverCollection> {

    @Override
    public ScriptResolverCollection createPlugin(ScriptObject object, ScriptAccount account) {
        return new ScriptResolverCollection(object, account);
    }

    @Override
    public void addPlugin(ScriptResolverCollection scriptPlugin) {
        CollectionManager.get().addCollection(scriptPlugin);
    }

    @Override
    public void removePlugin(ScriptResolverCollection scriptPlugin) {
        CollectionManager.get().removeCollection(scriptPlugin);
    }
}
