package com.kpmg.core.models;

import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface MainNavigationSecondaryItem
    extends NavigationItemWithChildren<MainNavigationTertiaryItem> {}
