import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SubRuleInListItemComponent } from '../list/sub-rule-in-list-item.component';
import { SubRuleInListItemDetailComponent } from '../detail/sub-rule-in-list-item-detail.component';
import { SubRuleInListItemUpdateComponent } from '../update/sub-rule-in-list-item-update.component';
import { SubRuleInListItemRoutingResolveService } from './sub-rule-in-list-item-routing-resolve.service';

const subRuleInListItemRoute: Routes = [
  {
    path: '',
    component: SubRuleInListItemComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SubRuleInListItemDetailComponent,
    resolve: {
      subRuleInListItem: SubRuleInListItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SubRuleInListItemUpdateComponent,
    resolve: {
      subRuleInListItem: SubRuleInListItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SubRuleInListItemUpdateComponent,
    resolve: {
      subRuleInListItem: SubRuleInListItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(subRuleInListItemRoute)],
  exports: [RouterModule],
})
export class SubRuleInListItemRoutingModule {}
