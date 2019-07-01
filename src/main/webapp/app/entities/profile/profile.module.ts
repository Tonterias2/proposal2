import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SpingularproposalSharedModule } from 'app/shared';
import {
  ProfileComponent,
  ProfileDetailComponent,
  ProfileUpdateComponent,
  ProfileDeletePopupComponent,
  ProfileDeleteDialogComponent,
  profileRoute,
  profilePopupRoute
} from './';

const ENTITY_STATES = [...profileRoute, ...profilePopupRoute];

@NgModule({
  imports: [SpingularproposalSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ProfileComponent,
    ProfileDetailComponent,
    ProfileUpdateComponent,
    ProfileDeleteDialogComponent,
    ProfileDeletePopupComponent
  ],
  entryComponents: [ProfileComponent, ProfileUpdateComponent, ProfileDeleteDialogComponent, ProfileDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SpingularproposalProfileModule {}
