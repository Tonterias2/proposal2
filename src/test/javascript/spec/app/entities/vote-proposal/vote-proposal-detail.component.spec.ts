/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SpingularproposalTestModule } from '../../../test.module';
import { VoteProposalDetailComponent } from 'app/entities/vote-proposal/vote-proposal-detail.component';
import { VoteProposal } from 'app/shared/model/vote-proposal.model';

describe('Component Tests', () => {
  describe('VoteProposal Management Detail Component', () => {
    let comp: VoteProposalDetailComponent;
    let fixture: ComponentFixture<VoteProposalDetailComponent>;
    const route = ({ data: of({ voteProposal: new VoteProposal(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SpingularproposalTestModule],
        declarations: [VoteProposalDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(VoteProposalDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VoteProposalDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.voteProposal).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
