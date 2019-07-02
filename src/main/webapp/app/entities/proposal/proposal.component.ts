import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IProposal } from 'app/shared/model/proposal.model';
import { AccountService } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { ProposalService } from './proposal.service';

import { IVoteProposal } from 'app/shared/model/vote-proposal.model';
import { VoteProposalService } from '../vote-proposal/vote-proposal.service';
import { IProfile } from 'app/shared/model/profile.model';
import { ProfileService } from '../profile/profile.service';

@Component({
  selector: 'jhi-proposal',
  templateUrl: './proposal.component.html'
})
export class ProposalComponent implements OnInit, OnDestroy {
  currentAccount: any;
  proposals: IProposal[];
  voteProposals: IVoteProposal[];
  profiles: IProfile[];
  profile: IProfile;

  error: any;
  success: any;
  eventSubscriber: Subscription;
  routeData: any;
  links: any;
  totalItems: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;
  owner: any;
  isAdmin: boolean;

  constructor(
    protected proposalService: ProposalService,
    protected voteProposalService: VoteProposalService,
    protected profileService: ProfileService,
    protected parseLinks: JhiParseLinks,
    protected jhiAlertService: JhiAlertService,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.previousPage = data.pagingParams.page;
      this.reverse = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
    });
  }

  loadAll() {
    this.proposalService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IProposal[]>) => {
          this.proposals = res.body;
          console.log('CONSOLOG: M:loadAll & O: this.proposals : ', this.proposals);
          this.proposals.forEach(proposal => {
            const query2 = {};
            query2['proposalId.equals'] = proposal.id;
            query2['queryParams'] = 1;
            //                    console.log( 'CONSOLOG: M:loadAll & O: query2 : ', query2 );
            this.voteProposalService.query(query2).subscribe(
              (res2: HttpResponse<IVoteProposal[]>) => {
                this.voteProposals = res2.body;
                console.log('CONSOLOG: M:loadAll & O: voteProposals : ', this.voteProposals);
                if (this.voteProposals != null) {
                  const arrayVoteProposals = [];
                  this.voteProposals.forEach(voteProposal => {
                    console.log('CONSOLOG: M:loadAll & O: arrayVoteProposals : ', arrayVoteProposals);
                    arrayVoteProposals.push(voteProposal.votePoints);
                  });
                }
                console.log('CONSOLOG: M:loadAll & O: this.voteProposals : ', this.voteProposals);
              },
              (res2: HttpErrorResponse) => this.onError(res2.message)
            );
          });
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  myProposals() {
    console.log('CONSOLOG: M:myProposals & DENTRO');
    const query = {};
    if (this.currentAccount.id != null) {
      query['profileId.equals'] = this.profile.id;
      query['queryParams'] = 1;
    }
    this.proposalService.query(query).subscribe(
      //        (res: HttpResponse<IChatRoom[]>) => this.paginateChatRooms(res.body, res.headers),
      (res: HttpResponse<IProposal[]>) => {
        this.paginateProposals(res.body, res.headers);
        console.log('CONSOLOG: M:myProposals & O: query : ', query);
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/proposal'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });
    this.loadAll();
  }

  clear() {
    this.page = 0;
    this.router.navigate([
      '/proposal',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
      this.owner = this.currentAccount.id;
      this.isAdmin = this.accountService.hasAnyAuthority(['ROLE_ADMIN']);
      const query = {};
      query['id.equals'] = this.currentAccount.id;
      query['queryParams'] = 1;
      //        console.log('CONSOLOG: M:ngOnInit & O: query : ', query);
      this.profileService.query(query).subscribe(
        (res: HttpResponse<IProfile[]>) => {
          this.profile = res.body[0];
          //            console.log('CONSOLOG: M:ngOnInit & O: this.profile : ', this.profile);
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    });
    this.registerChangeInProposals();
  }

  study() {
    const query = {};
    query['proposalType.equals'] = 'STUDY';
    query['queryParams'] = 1;
    console.log('CONSOLOG: M:study & O: query : ', query);
    this.byTypeQuery(query);
  }
  approved() {
    const query = {};
    query['proposalType.equals'] = 'APPROVED';
    query['queryParams'] = 1;
    console.log('CONSOLOG: M:study & O: query : ', query);
    this.byTypeQuery(query);
  }
  development() {
    const query = {};
    query['proposalType.equals'] = 'DEVELOPMENT';
    query['queryParams'] = 1;
    console.log('CONSOLOG: M:study & O: query : ', query);
    this.byTypeQuery(query);
  }
  production() {
    const query = {};
    query['proposalType.equals'] = 'PRODUCTION';
    query['queryParams'] = 1;
    console.log('CONSOLOG: M:study & O: query : ', query);
    this.byTypeQuery(query);
  }
  byTypeQuery(query) {
    this.proposalService.query(query).subscribe(
      //        (res: HttpResponse<IChatRoom[]>) => this.paginateChatRooms(res.body, res.headers),
      (res: HttpResponse<IProposal[]>) => {
        this.paginateProposals(res.body, res.headers);
        console.log('CONSOLOG: M:myProposals & O: query : ', query);
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IProposal) {
    return item.id;
  }

  registerChangeInProposals() {
    this.eventSubscriber = this.eventManager.subscribe('proposalListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateProposals(data: IProposal[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.proposals = data;
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
