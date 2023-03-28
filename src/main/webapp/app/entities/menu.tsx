import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/report">
        <Translate contentKey="global.menu.entities.report" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/report-log">
        <Translate contentKey="global.menu.entities.reportLog" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/report-comments">
        <Translate contentKey="global.menu.entities.reportComments" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/victim">
        <Translate contentKey="global.menu.entities.victim" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
