/**
 *  This class provides methods to access to PocketComputer of ComputerCraft.
 *  These methods are NOT official method.
 */

package iunius118.mods.pocketcomputeraccess;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PocketComputerAccess {

	private static final String CLASS_NAME_POCKET_COMPUTER = "dan200.computercraft.shared.pocket.items.ItemPocketComputer";
	private static final String METHOD_NAME_CREATE_SERVER_COMPUTER = "createServerComputer";
	private static final String METHOD_NAME_QUEUE_EVENT = "queueEvent";

	/**
	 * Causes an event to a Pocket Computer.<br>
	 * @param world     An World instance.
	 * @param itemStack An ItemStack instance of ItemPocketComputer.
	 * @param event     A string identifying the type of event.
	 * @param arguments An array of extra arguments to the event.
	 */
	public static void queueEvent( net.minecraft.world.World world, net.minecraft.item.ItemStack itemStack, String event, Object[] arguments ) {
		// Server side only
		if ( world == null || world.isRemote || itemStack == null || itemStack.getItem() == null || event == null ) {
			return;
		}

		net.minecraft.item.Item item = itemStack.getItem();

		if ( ! item.getClass().getName().equals( CLASS_NAME_POCKET_COMPUTER ) ) {
			return;
		}

		Method methodCreateServerComputer;
		Method methodQueueEvent;
		Object serverComputer;

		try {
			methodCreateServerComputer = item.getClass().getDeclaredMethod( METHOD_NAME_CREATE_SERVER_COMPUTER,
					new Class[]{ net.minecraft.world.World.class, net.minecraft.inventory.IInventory.class, net.minecraft.item.ItemStack.class } );

			if ( methodCreateServerComputer == null ) {
				return;
			}

			methodCreateServerComputer.setAccessible( true );
			serverComputer = methodCreateServerComputer.invoke( item, new Object[]{ world, null, itemStack } );

			if ( serverComputer == null ) {
				return;
			}

			methodQueueEvent = serverComputer.getClass().getMethod( METHOD_NAME_QUEUE_EVENT,
					new Class[]{ String.class, Object[].class } );

			if ( methodQueueEvent == null ) {
				return;
			}

			methodQueueEvent.invoke( serverComputer, new Object[]{ event, arguments } );

		} catch ( SecurityException e ) {
			e.printStackTrace();

		} catch ( NoSuchMethodException e ) {
			e.printStackTrace();

		} catch ( IllegalArgumentException e ) {
			e.printStackTrace();

		} catch ( IllegalAccessException e ) {
			e.printStackTrace();

		} catch ( InvocationTargetException e ) {
			e.printStackTrace();
		}

	}

	/**
	 * Causes an event to a Pocket Computer.<br>
	 * @param world     An World instance.
	 * @param itemStack An ItemStack instance of ItemPocketComputer.
	 * @param event     A string identifying the type of event.
	 */
	public static void queueEvent( net.minecraft.world.World world, net.minecraft.item.ItemStack itemStack, String event ) {
		queueEvent( world, itemStack, event, null );
	}

}
