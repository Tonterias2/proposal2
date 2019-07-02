import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IVoteProposal } from 'app/shared/model/vote-proposal.model';
import { AccountService } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { VoteProposalService } from './vote-proposal.service';

@Component({
  selector: 'jhi-vote-proposal',
  templateUrl: './vote-proposal.component.html'
})
export class VoteProposalComponent implements OnInit, OnDestroy {
  currentAccount: any;
  voteProposals: IVoteProposal[];
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
    protected voteProposalService: VoteProposalService,
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
    this.voteProposalService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IVoteProposal[]>) => this.paginateVoteProposals(res.body, res.headers),
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
    this.router.navigate(['/vote-proposal'], {
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
      '/vote-proposal',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  ngOnInit() {
    //    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
      this.owner = this.currentAccount.id;
      this.isAdmin = this.accountService.hasAnyAuthority(['ROLE_ADMIN']);
      if (!this.accountService.hasAnyAuthority(['ROLE_ADMIN'])) {
        const query = {};
        query['profileId.equals'] = this.currentAccount.id;
        query['queryParams'] = 1;
        //                    console.log( 'CONSOLOG: M:loadAll & O: query2 : ', query2 );
        this.voteProposalService.query(query).subscribe(
          (res: HttpResponse<IVoteProposal[]>) => {
            this.voteProposals = res.body;
            console.log('CONSOLOG: M:loadAll & O: voteProposals : ', this.voteProposals);
            //              if (this.voteProposals != null) {
            //                const arrayVoteProposals = [];
            //                this.voteProposals.forEach(voteProposal => {
            //                  console.log('CONSOLOG: M:loadAll & O: arrayVoteProposals : ', arrayVoteProposals);
            //                  arrayVoteProposals.push(voteProposal.votePoints);
            //                });
            //              }
            //              console.log('CONSOLOG: M:loadAll & O: this.voteProposals : ', this.voteProposals);
          },
          (res: HttpErrorResponse) => this.onError(res.message)
        );
      } else {
        this.loadAll();
      }
    });
    this.registerChangeInVoteProposals();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IVoteProposal) {
    return item.id;
  }

  registerChangeInVoteProposals() {
    this.eventSubscriber = this.eventManager.subscribe('voteProposalListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateVoteProposals(data: IVoteProposal[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.voteProposals = data;
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
