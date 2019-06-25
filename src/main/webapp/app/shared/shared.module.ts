import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { SpingularproposalSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [SpingularproposalSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [SpingularproposalSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SpingularproposalSharedModule {
  static forRoot() {
    return {
      ngModule: SpingularproposalSharedModule
    };
  }
}
