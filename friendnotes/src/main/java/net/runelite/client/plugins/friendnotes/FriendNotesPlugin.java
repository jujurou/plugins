/*
 * Copyright (c) 2018, Rheon <https://github.com/Rheon-D>
 * Copyright (c) 2018, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *     list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package net.runelite.client.plugins.friendnotes;

import com.google.common.base.Strings;
import java.awt.Color;
import javax.annotation.Nullable;
import javax.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Friend;
import net.runelite.api.MenuOpcode;
import net.runelite.api.Nameable;
import net.runelite.api.events.FriendRemoved;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.NameableNameChanged;
import net.runelite.api.util.Text;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.chatbox.ChatboxPanelManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ColorUtil;
import org.pf4j.Extension;

@Extension
@PluginDescriptor(
	name = "Friend Notes",
	description = "Store notes about your friends",
	type = PluginType.MISCELLANEOUS
)
@Slf4j
public class FriendNotesPlugin extends Plugin
{
	private static final String CONFIG_GROUP = "friendNotes";
	private static final int CHARACTER_LIMIT = 128;
	private static final String KEY_PREFIX = "note_";
	private static final String ADD_NOTE = "Add Note";
	private static final String EDIT_NOTE = "Edit Note";
	private static final String NOTE_PROMPT_FORMAT = "%s's Notes<br>" +
		ColorUtil.prependColorTag("(Limit %s Characters)", new Color(0, 0, 170));

	@Inject
	private Client client;

	@Inject
	private ConfigManager configManager;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private FriendNoteOverlay overlay;

	@Inject
	private ChatboxPanelManager chatboxPanelManager;

	@Getter(AccessLevel.PACKAGE)
	private HoveredFriend hoveredFriend = null;

	@Override
	protected void startUp()
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(overlay);
	}

	/**
	 * Set a friend note, or unset by passing a null/empty note.
	 */
	private void setFriendNote(String displayName, String note)
	{
		if (Strings.isNullOrEmpty(note))
		{
			configManager.unsetConfiguration(CONFIG_GROUP, KEY_PREFIX + displayName);
		}
		else
		{
			configManager.setConfiguration(CONFIG_GROUP, KEY_PREFIX + displayName, note);
		}
	}

	/**
	 * Get the friend note of a display name, or null if no friend note exists for it.
	 */
	@Nullable
	private String getFriendNote(String displayName)
	{
		return configManager.getConfiguration(CONFIG_GROUP, KEY_PREFIX + displayName);
	}

	/**
	 * Migrate a friend note to a new display name, and remove the previous one.
	 * If current name already has a note, or previous name had none, do nothing.
	 */
	private void migrateFriendNote(String currentDisplayName, String prevDisplayName)
	{
		final String currentNote = getFriendNote(currentDisplayName);
		if (currentNote == null)
		{
			final String prevNote = getFriendNote(prevDisplayName);
			if (prevNote != null)
			{
				log.debug("Update friend's username: '{}' -> '{}'", prevDisplayName, currentDisplayName);
				setFriendNote(prevDisplayName, null);
				setFriendNote(currentDisplayName, prevNote);
			}
		}
	}

	/**
	 * Set the currently hovered display name, if a friend note exists for it.
	 */
	private void setHoveredFriend(String displayName)
	{
		hoveredFriend = null;

		if (!Strings.isNullOrEmpty(displayName))
		{
			final String note = getFriendNote(displayName);
			if (note != null)
			{
				hoveredFriend = new HoveredFriend(displayName, note);
			}
		}
	}

	@Subscribe
	private void onMenuEntryAdded(MenuEntryAdded event)
	{
		final int groupId = WidgetInfo.TO_GROUP(event.getParam1());

		// Look for "Message" on friends list
		if (groupId == WidgetInfo.FRIENDS_LIST.getGroupId() && event.getOption().equals("Message"))
		{
			// Friends have color tags
			setHoveredFriend(Text.toJagexName(Text.removeTags(event.getTarget())));

			// Build "Add Note" or "Edit Note" menu entry
			// Add menu entry
			client.insertMenuItem(
				hoveredFriend == null || hoveredFriend.getNote() == null ? ADD_NOTE : EDIT_NOTE,
				event.getTarget(),
				MenuOpcode.RUNELITE.getId(),
				0,
				event.getParam0(),
				event.getParam1(),
				false
			);
		}
		else if (hoveredFriend != null)
		{
			hoveredFriend = null;
		}
	}

	@Subscribe
	private void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (WidgetInfo.TO_GROUP(event.getParam1()) == WidgetInfo.FRIENDS_LIST.getGroupId())
		{
			if (Strings.isNullOrEmpty(event.getTarget()))
			{
				return;
			}

			// Handle clicks on "Add Note" or "Edit Note"
			if (event.getOption().equals(ADD_NOTE) || event.getOption().equals(EDIT_NOTE))
			{
				event.consume();

				//Friends have color tags
				final String sanitizedTarget = Text.toJagexName(Text.removeTags(event.getTarget()));
				final String note = getFriendNote(sanitizedTarget);

				// Open the new chatbox input dialog
				chatboxPanelManager.openTextInput(String.format(NOTE_PROMPT_FORMAT, sanitizedTarget, CHARACTER_LIMIT))
					.value(Strings.nullToEmpty(note))
					.onDone((content) ->
					{
						if (content == null)
						{
							return;
						}

						content = Text.removeTags(content).trim();
						log.debug("Set note for '{}': '{}'", sanitizedTarget, content);
						setFriendNote(sanitizedTarget, content);
					}).build();
			}
		}

	}

	@Subscribe
	private void onNameableNameChanged(NameableNameChanged event)
	{
		final Nameable nameable = event.getNameable();

		if (nameable instanceof Friend)
		{
			// Migrate a friend's note to their new display name
			final Friend friend = (Friend) nameable;
			String name = friend.getName();
			String prevName = friend.getPrevName();

			if (prevName != null)
			{
				migrateFriendNote(
					Text.toJagexName(name),
					Text.toJagexName(prevName)
				);
			}
		}
	}

	@Subscribe
	private void onFriendRemoved(FriendRemoved event)
	{
		// Delete a friend's note if they are removed
		final String displayName = Text.toJagexName(event.getName());
		log.debug("Remove friend: '{}'", displayName);
		setFriendNote(displayName, null);
	}
}
