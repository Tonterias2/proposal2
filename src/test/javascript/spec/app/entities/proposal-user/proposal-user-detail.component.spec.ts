/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SpingularproposalTestModule } from '../../../test.module';
import { ProposalUserDetailComponent } from 'app/entities/proposal-user/proposal-user-detail.component';
import { ProposalUser } from 'app/shared/model/proposal-user.model';

describe('Component Tests', () => {
  describe('ProposalUser Management Detail Component', () => {
    let comp: ProposalUserDetailComponent;
    let fixture: ComponentFixture<ProposalUserDetailComponent>;
    const route = ({ data: of({ proposalUser: new ProposalUser(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpingularproposalTestModule],
        declarations: [ProposalUserDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ProposalUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProposalUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.proposalUser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
