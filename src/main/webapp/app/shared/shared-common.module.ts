import { NgModule } from '@angular/core';

import { SpingularproposalSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
  imports: [SpingularproposalSharedLibsModule],
  declarations: [JhiAlertComponent, JhiAlertErrorComponent],
  exports: [SpingularproposalSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class SpingularproposalSharedCommonModule {}
